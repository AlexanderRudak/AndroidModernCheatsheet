package dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen


import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.rudak.androidcheatsheet.core.ui.components.ErrorContent
import dev.rudak.androidcheatsheet.core.ui.components.LoadingView
import dev.rudak.androidcheatsheet.core.ui.components.ProductsDemoContent
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.MvvmNews
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.MvvmUiState
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel.MvvmViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MvvmScreen(
    viewModel: MvvmViewModel = koinViewModel(),
    onNavigate: (Long) -> Unit,
    onNavigateToHistory: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(enabled = uiState.errorMessage != null) {
        viewModel.dismissError()
    }

    LaunchedEffect(Unit) {
        viewModel.news.collect { news ->
            when (news) {
                is MvvmNews.NavigateToDetails -> {
                    onNavigate(news.productId)
                }

                MvvmNews.NavigateToHistory -> {
                    onNavigateToHistory()
                }
            }
        }
    }

    MvvmContent(
        uiState = uiState,
        onRetryClick = {
            viewModel.loadProducts(force = true)
        },
        onLoadNextPage = viewModel::loadNextPage,
        onErrorClick = viewModel::loadProductsWithError,
        onProductClick = viewModel::onProductClick,
        onShowOnlyFavoritesChange = viewModel::setShowOnlyFavorites,
        onFavoriteClick = viewModel::toggleFavorite,
        onRecentlyViewedClick = viewModel::onRecentlyViewedClick,
        onSettingsClick = onSettingsClick,
    )
}

@Composable
fun MvvmContent(
    uiState: MvvmUiState,
    onRetryClick: () -> Unit,
    onLoadNextPage: () -> Unit,
    onErrorClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onShowOnlyFavoritesChange: (Boolean) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onRecentlyViewedClick: () -> Unit = {},
    onSettingsClick: () -> Unit,
) {
    when {
        uiState.isLoading && uiState.products.isEmpty() -> {
            LoadingView()
        }

        uiState.errorMessage != null -> {
            ErrorContent(
                message = uiState.errorMessage,
                onRetryClick = onRetryClick,
            )
        }

        else -> {
            ProductsDemoContent(
                title = "MVVM example",
                description = "Screen observes StateFlow from ViewModel.",
                products = uiState.products,
                showOnlyFavorites = uiState.showOnlyFavorites,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                onShowOnlyFavoritesChange = onShowOnlyFavoritesChange,
                onReloadClick = onRetryClick,
                onErrorClick = onErrorClick,
                onLoadNextPage = onLoadNextPage,
                onProductClick = onProductClick,
                onFavoriteClick = onFavoriteClick,
                onRecentlyViewedClick = onRecentlyViewedClick,
                onSettingsClick = onSettingsClick,
            )
        }
    }
}
