package dev.rudak.androidcheatsheet.feature.mvi.presentation.news

sealed interface MviNews {

    data class NavigateToDetails(
        val productId: Long,
    ) : MviNews

    data class ShowMessage(
        val message: String,
    ) : MviNews
}