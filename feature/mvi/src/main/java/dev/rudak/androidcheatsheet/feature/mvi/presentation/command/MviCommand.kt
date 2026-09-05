package dev.rudak.androidcheatsheet.feature.mvi.presentation.command

sealed interface MviCommand {

    data class LoadProducts(
        val page: Int,
        val pageSize: Int,
    ) : MviCommand

    data class LoadProductsWithError(
        val page: Int,
        val pageSize: Int,
    ) : MviCommand

    data class ToggleFavorite(
        val productId: Long,
    ) : MviCommand

    data class SetShowOnlyFavorites(
        val value: Boolean,
    ) : MviCommand
}