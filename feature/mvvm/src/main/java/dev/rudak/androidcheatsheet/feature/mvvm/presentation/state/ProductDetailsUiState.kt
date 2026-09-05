package dev.rudak.androidcheatsheet.feature.mvvm.presentation.state

import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.model.ProductUiModel

sealed interface ProductDetailsUiState {

    data object Loading : ProductDetailsUiState

    data class Content(
        val product: ProductCardUiModel,
        val recommendedProducts: List<ProductCardUiModel>,
    ) : ProductDetailsUiState

    data class Error(
        val message: String,
    ) : ProductDetailsUiState
}