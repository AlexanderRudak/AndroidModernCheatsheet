package dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rudak.androidcheatsheet.core.ui.components.ErrorContent
import dev.rudak.androidcheatsheet.core.ui.components.ErrorView
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
) {
    val uiState by viewModel.uiState.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.loadProducts()
//    }

    LaunchedEffect(Unit) {
        viewModel.news.collect { news ->
            when (news) {
                is MvvmNews.NavigateToDetails -> {
                    onNavigate(news.productId)
                }

//                is MvvmNews.ShowMessage -> {
//                    // пока можно ничего не делать
//                }
            }
        }
    }

//    MvvmContent(
//        uiState = uiState,
//        onRetryClick = {
//            viewModel.loadProducts(force = true)
//        },
//        onErrorClick = viewModel::loadProductsWithError,
//        onProductClick = viewModel::onProductClick,
//        onShowOnlyFavoritesChange = viewModel::setShowOnlyFavorites,
//        onFavoriteClick = viewModel::toggleFavorite,
//    )
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
) {
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
    )
}
//
//@Composable
//fun MvvmContent(
//    uiState: MvvmUiState,
//    onRetryClick: () -> Unit,
//    onLoadNextPage: () -> Unit,
//    onErrorClick: () -> Unit,
//    onProductClick: (Long) -> Unit,
//    onShowOnlyFavoritesChange: (Boolean) -> Unit,
//    onFavoriteClick: (Long) -> Unit,
//) {
//    when {
//        uiState.isLoading && uiState.products.isEmpty() -> {
//            LoadingView()
//        }
//
//        uiState.errorMessage != null && uiState.products.isEmpty() -> {
//            ErrorContent(
//                message = uiState.errorMessage,
//                onRetryClick = onRetryClick,
//            )
//        }
//
//        else -> {
//            ProductsDemoContent(
//                title = "MVVM example",
//                description = "Screen observes StateFlow from ViewModel.",
//                products = uiState.products,
//                onReloadClick = onRetryClick,
//                onErrorClick = onErrorClick,
//                onProductClick = onProductClick,
//                showOnlyFavorites = uiState.showOnlyFavorites,
//                onShowOnlyFavoritesChange = onShowOnlyFavoritesChange,
//                onFavoriteClick = onFavoriteClick,
//                isLoadingNextPage = uiState.isLoading,
//                errorMessage = uiState.errorMessage,
//                onLoadNextPage = onLoadNextPage,
//            )
//        }
//    }
//}

//@Composable
//fun MvvmContent(
//    uiState: MvvmUiState,
//    onRetryClick: () -> Unit,
//    onErrorClick: () -> Unit,
//    onProductClick: (Long) -> Unit,
//    onShowOnlyFavoritesChange: (Boolean) -> Unit,
//    onFavoriteClick: (Long) -> Unit,
//) {
//    when (uiState) {
//        MvvmUiState.Loading -> {
//            LoadingView()
//        }
//
//        is MvvmUiState.Error -> {
//            MvvmErrorContent(
//                message = uiState.message,
//                onRetryClick = onRetryClick,
//            )
//        }
//
//        is MvvmUiState.Content -> {
//            ProductsDemoContent(
//                title = "MVVM example",
//                description = "Screen observes StateFlow from ViewModel.",
//                products = uiState.products,
//                onReloadClick = onRetryClick,
//                onErrorClick = onErrorClick,
//                onProductClick = onProductClick,
//                showOnlyFavorites = uiState.showOnlyFavorites,
//                onShowOnlyFavoritesChange = onShowOnlyFavoritesChange,
//                onFavoriteClick = onFavoriteClick,
//            )
//        }
//
//    }
//}
//
//@Composable
//private fun MvvmErrorContent(
//    message: String,
//    onRetryClick: () -> Unit,
//) {
//    Column(
//        modifier = Modifier.statusBarsPadding().padding(16.dp),
//    ) {
//        ErrorView(message = message)
//
//        Button(
//            modifier = Modifier.padding(top = 16.dp),
//            onClick = onRetryClick,
//        ) {
//            Text(text = "Retry")
//        }
//    }
//}
