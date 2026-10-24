package dev.rudak.androidcheatsheet.feature.mvvm.presentation.state

sealed interface RecentlyViewedNews {
    data object ShowClearHistoryError : RecentlyViewedNews
}
