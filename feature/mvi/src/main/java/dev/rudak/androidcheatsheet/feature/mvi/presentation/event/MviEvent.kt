package dev.rudak.androidcheatsheet.feature.mvi.presentation.event

import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.feature.mvi.presentation.model.ProductUiModel

sealed interface MviEvent {

//    data object LoadProducts : MviEvent
    data class LoadProducts(
        val page: Int,
    ) : MviEvent

    data object LoadWithErrorClicked : MviEvent

    data class ProductClicked(
        val productId: Long,
    ) : MviEvent

    data class FavoriteClicked(
        val productId: Long,
    ) : MviEvent

    data class ShowOnlyFavoritesChanged(
        val value: Boolean,
    ) : MviEvent

    data class ProductsLoaded(
        val products: List<ProductCardUiModel>,
        val currentPage: Int,
        val totalPages: Int,
    ) : MviEvent

    data class ProductsLoadingFailed(
        val message: String,
    ) : MviEvent

    data class FavoriteUpdated(
        val productId: Long,
    ) : MviEvent

    data class ShowOnlyFavoritesSaved(
        val value: Boolean,
    ) : MviEvent
}