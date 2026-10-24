package dev.rudak.androidcheatsheet.feature.mvvm.presentation.state

import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel

sealed interface RecentlyViewedUiState {

    data object Loading : RecentlyViewedUiState

    data class Content(
        val products: List<ProductCardUiModel>,
    ) : RecentlyViewedUiState

    data object Empty : RecentlyViewedUiState

    data class Error(
        val message: String,
    ) : RecentlyViewedUiState
}
