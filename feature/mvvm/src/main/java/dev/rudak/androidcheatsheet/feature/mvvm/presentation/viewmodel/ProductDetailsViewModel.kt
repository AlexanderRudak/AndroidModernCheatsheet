package dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rudak.androidcheatsheet.core.common.result.runSuspendCatching
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductByIdUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductDetailsInteractor
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetRecommendedProductsUseCase
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.mapper.toUiModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.ProductDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productId: Long,
    private val getProductDetailsInteractor: GetProductDetailsInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

//    fun loadProduct() {
//        viewModelScope.launch {
//            _uiState.value = ProductDetailsUiState.Loading
//
//            val product = getProductByIdUseCase(productId)
//
//            _uiState.value = if (product != null) {
//                ProductDetailsUiState.Content(product.toUiModel())
//            } else {
//                ProductDetailsUiState.Error("Product not found")
//            }
//        }
//    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading

            runSuspendCatching {
                getProductDetailsInteractor(productId)
            }
                .onSuccess { details ->
                    val product = details.product

                    _uiState.value = if (product != null) {
                        ProductDetailsUiState.Content(
                            product = product.toUiModel(),
                            recommendedProducts = details.recommendedProducts
                                .map { it.toUiModel() },
                        )
                    } else {
                        ProductDetailsUiState.Error(
                            message = "Product not found",
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.value = ProductDetailsUiState.Error(
                        message = throwable.message ?: "Unknown error",
                    )
                }
        }
    }
}