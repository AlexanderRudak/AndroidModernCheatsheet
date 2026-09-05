package dev.rudak.androidcheatsheet.feature.mvvm.presentation.state

sealed interface MvvmNews {

    data class NavigateToDetails(
        val productId: Long,
    ) : MvvmNews

}