package dev.rudak.androidcheatsheet.feature.mvvm.presentation.state

sealed interface ProductDetailsNews {
    data object ShowHistorySaveError : ProductDetailsNews
}
