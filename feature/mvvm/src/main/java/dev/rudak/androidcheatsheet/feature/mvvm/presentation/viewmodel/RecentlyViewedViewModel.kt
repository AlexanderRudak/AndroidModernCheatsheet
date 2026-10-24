package dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.usecase.shop.ClearRecentlyViewedProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ObserveRecentlyViewedProductsUseCase
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.mapper.toUiModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.RecentlyViewedNews
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.RecentlyViewedUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecentlyViewedViewModel(
    observeRecentlyViewedProductsUseCase: ObserveRecentlyViewedProductsUseCase,
    private val clearRecentlyViewedProductsUseCase: ClearRecentlyViewedProductsUseCase,
) : ViewModel() {

    val uiState: StateFlow<RecentlyViewedUiState> = observeRecentlyViewedProductsUseCase()
        .map<List<Product>, RecentlyViewedUiState> { products ->
            if (products.isEmpty()) {
                RecentlyViewedUiState.Empty
            } else {
                RecentlyViewedUiState.Content(
                    products = products.map { product -> product.toUiModel() },
                )
            }
        }
        .catch { throwable ->
            emit(
                RecentlyViewedUiState.Error(
                    message = throwable.message ?: "Unknown error",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecentlyViewedUiState.Loading,
        )

    private val _news = MutableSharedFlow<RecentlyViewedNews>()
    val news = _news.asSharedFlow()

    private var clearJob: Job? = null

    fun onClearHistoryClick() {
        if (clearJob?.isActive == true) return

        clearJob = viewModelScope.launch {
            try {
                clearRecentlyViewedProductsUseCase()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _news.emit(RecentlyViewedNews.ShowClearHistoryError)
            }
        }
    }
}
