package dev.rudak.androidcheatsheet.feature.mvi.presentation.command

sealed interface MviCommand {

    sealed interface Action : MviCommand

    data class LoadProducts(
        val page: Int,
        val pageSize: Int,
    ) : Action

    data class LoadProductsWithError(
        val page: Int,
        val pageSize: Int,
    ) : Action

    data class ToggleFavorite(
        val productId: Long,
    ) : Action

    data class SetShowOnlyFavorites(
        val value: Boolean,
    ) : Action


    sealed interface Analytic : MviCommand {

        data class ProductClicked(
            val productId: Long,
        ) : Analytic

        data class FavoriteClicked(
            val productId: Long,
            val isFavorite: Boolean,
        ) : Analytic

    }
}