package dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rudak.androidcheatsheet.core.common.result.runSuspendCatching
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.usecase.preferences.ObserveShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.preferences.SetShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsWithErrorUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ToggleFavoriteUseCase
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.mapper.toUiModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.MvvmNews
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.MvvmUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MvvmViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsWithErrorUseCase: GetProductsWithErrorUseCase,
    private val observeShowOnlyFavoritesUseCase: ObserveShowOnlyFavoritesUseCase,
    private val setShowOnlyFavoritesUseCase: SetShowOnlyFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MvvmUiState())
    val uiState: StateFlow<MvvmUiState> = _uiState.asStateFlow()

    private val productsFlow = MutableStateFlow<List<Product>>(emptyList())

    private val _news = MutableSharedFlow<MvvmNews>()
    val news = _news.asSharedFlow()

    init {
        observeProducts()
        loadProducts()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            combine(
                productsFlow,
                observeShowOnlyFavoritesUseCase(),
            ) { products, showOnlyFavorites ->

                val productUiModels = products.map { product ->
                    product.toUiModel()
                }

                val visibleProducts = if (showOnlyFavorites) {
                    productUiModels.filter { product ->
                        product.isFavorite
                    }
                } else {
                    productUiModels
                }

                visibleProducts to showOnlyFavorites
            }.collect { (visibleProducts, showOnlyFavorites) ->
                _uiState.update { currentState ->
                    currentState.copy(
                        products = visibleProducts,
                        showOnlyFavorites = showOnlyFavorites,
                    )
                }
            }
        }
    }

    fun loadProducts(force: Boolean = false) {
        if (_uiState.value.isLoading) {
            return
        }

        if (!force && productsFlow.value.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            runSuspendCatching {
                getProductsUseCase(
                    page = FIRST_PAGE,
                    pageSize = PAGE_SIZE,
                )
            }
                .onSuccess { productsPage ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentPage = productsPage.currentPage,
                            totalPages = productsPage.totalPages,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }

                    productsFlow.value = productsPage.products
                }
                .onFailure { throwable ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = throwable.message
                                ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun loadNextPage() {
        val currentState = _uiState.value

        if (currentState.isLoading) {
            return
        }

        if (currentState.currentPage >= currentState.totalPages) {
            return
        }

        val nextPage = currentState.currentPage + 1

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            runSuspendCatching {
                getProductsUseCase(
                    page = nextPage,
                    pageSize = PAGE_SIZE,
                )
            }
                .onSuccess { productsPage ->
                    productsFlow.value =
                        productsFlow.value + productsPage.products

                    _uiState.update { state ->
                        state.copy(
                            currentPage = productsPage.currentPage,
                            totalPages = productsPage.totalPages,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = throwable.message
                                ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun loadProductsWithError() {
        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            runSuspendCatching {
                getProductsWithErrorUseCase(
                    page = FIRST_PAGE,
                    pageSize = PAGE_SIZE,
                )
            }
                .onFailure { throwable ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = throwable.message
                                ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun setShowOnlyFavorites(value: Boolean) {
        viewModelScope.launch {
            setShowOnlyFavoritesUseCase(value)
        }
    }

    fun toggleFavorite(productId: Long) {
        viewModelScope.launch {
            runSuspendCatching {
                toggleFavoriteUseCase(productId)
            }
                .onSuccess {
                    productsFlow.value =
                        productsFlow.value.map { product ->
                            if (product.id == productId) {
                                product.copy(
                                    isFavorite = !product.isFavorite,
                                )
                            } else {
                                product
                            }
                        }
                }
                .onFailure { throwable ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = throwable.message
                                ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun onProductClick(productId: Long) {
        viewModelScope.launch {
            _news.emit(
                MvvmNews.NavigateToDetails(productId),
            )
        }
    }

    private companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 10
    }
}

//
//class MvvmViewModel(
//    //private val observeProductsUseCase: ObserveProductsUseCase,
//    private val getProductsUseCase: GetProductsUseCase,
//    private val getProductsWithErrorUseCase: GetProductsWithErrorUseCase,
//    private val observeShowOnlyFavoritesUseCase: ObserveShowOnlyFavoritesUseCase,
//    private val setShowOnlyFavoritesUseCase: SetShowOnlyFavoritesUseCase,
//    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow<MvvmUiState>(MvvmUiState.Loading)
//    val uiState: StateFlow<MvvmUiState> = _uiState.asStateFlow()
//
//    private val productsFlow = MutableStateFlow<List<Product>>(emptyList())
//
//    private val _news = MutableSharedFlow<MvvmNews>()
//    val news = _news.asSharedFlow()
//
//    init {
//        observeProducts()
//        loadProducts()
//    }
//
//    private fun observeProducts() {
//        viewModelScope.launch {
//            combine(
//                productsFlow,
//                observeShowOnlyFavoritesUseCase(),
//            ) { products, showOnlyFavorites ->
//                val productUiModels = products.map { product ->
//                    product.toUiModel()
//                }
//
//                val visibleProducts = if (showOnlyFavorites) {
//                    productUiModels.filter { product -> product.isFavorite }
//                } else {
//                    productUiModels
//                }
//
//                MvvmUiState.Content(
//                    products = visibleProducts,
//                    showOnlyFavorites = showOnlyFavorites,
//                )
//            }.collect { state ->
//                _uiState.value = state
//            }
//        }
//    }
//
//    fun loadProducts(force: Boolean = false) {
//        if (!force && productsFlow.value.isNotEmpty()) {
//            return
//        }
//
//        viewModelScope.launch {
//            _uiState.value = MvvmUiState.Loading
//
//            runSuspendCatching {
//                getProductsUseCase()
//            }
//                .onSuccess { products ->
//                    productsFlow.value = products
//                }
//                .onFailure { throwable ->
//                    _uiState.value = MvvmUiState.Error(
//                        message = throwable.message ?: "Unknown error",
//                    )
//                }
//        }
//    }
//
//
//    fun setShowOnlyFavorites(value: Boolean) {
//        viewModelScope.launch {
//            setShowOnlyFavoritesUseCase(value)
//        }
//    }
//
//    fun toggleFavorite(productId: Long) {
//        viewModelScope.launch {
//            runSuspendCatching {
//                toggleFavoriteUseCase(productId)
//            }
//                .onSuccess {
//                    productsFlow.value = productsFlow.value.map { product ->
//                        if (product.id == productId) {
//                            product.copy(isFavorite = !product.isFavorite)
//                        } else {
//                            product
//                        }
//                    }
//                }
//                .onFailure { throwable ->
//                    _uiState.value = MvvmUiState.Error(
//                        message = throwable.message ?: "Unknown error",
//                    )
//                }
//        }
//    }
//
//
//
//    fun loadProductsWithError() {
//        viewModelScope.launch {
//            _uiState.value = MvvmUiState.Loading
//
//            runSuspendCatching {
//                getProductsWithErrorUseCase()
//            }.onFailure { throwable ->
//                _uiState.value = MvvmUiState.Error(
//                    message = throwable.message ?: "Unknown error",
//                )
//            }
//        }
//    }
//
//    fun onProductClick(productId: Long) {
//        viewModelScope.launch {
//            _news.emit(
//                MvvmNews.NavigateToDetails(productId)
//            )
//        }
//    }
//
//}