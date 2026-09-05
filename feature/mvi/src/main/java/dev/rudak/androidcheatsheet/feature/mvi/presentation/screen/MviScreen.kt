package dev.rudak.androidcheatsheet.feature.mvi.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.rudak.androidcheatsheet.core.ui.components.PagedProductsDemoContent
import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.news.MviNews
import dev.rudak.androidcheatsheet.feature.mvi.presentation.state.MviState
import dev.rudak.androidcheatsheet.feature.mvi.presentation.viewmodel.MviViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MviScreen(
    viewModel: MviViewModel = koinViewModel(),
    onNavigateToProductDetails: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagedProducts = viewModel.pagedProducts.collectAsLazyPagingItems()

    LaunchedEffect(viewModel) {
        viewModel.news.collect { news ->
            when (news) {
                is MviNews.NavigateToDetails -> {
                    onNavigateToProductDetails(news.productId)
                }

                is MviNews.ShowMessage -> Unit
            }
        }
    }

    MviContent(
        state = state,
        pagedProducts = pagedProducts,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun MviContent(
    state: MviState,
    pagedProducts: LazyPagingItems<ProductCardUiModel>,
    onEvent: (MviEvent) -> Unit,
) {
    PagedProductsDemoContent(
        title = "MVI example",
        description = "Screen sends Events, Reducer changes State, Actor executes Commands.",
        products = pagedProducts,
        showOnlyFavorites = state.showOnlyFavorites,
        onShowOnlyFavoritesChange = { value ->
            onEvent(
                MviEvent.ShowOnlyFavoritesChanged(value),
            )
        },
        onErrorClick = {
            onEvent(MviEvent.LoadWithErrorClicked)
        },
        onProductClick = { productId ->
            onEvent(
                MviEvent.ProductClicked(productId),
            )
        },
        onFavoriteClick = { productId ->
            onEvent(
                MviEvent.FavoriteClicked(productId),
            )
        },
    )
}

//@Composable
//fun MviContent(
//    state: MviState,
//    onEvent: (MviEvent) -> Unit,
//) {
//    ProductsDemoContent(
//        title = "MVI example",
//        description = "Screen sends Events, Reducer changes State, Actor executes Commands.",
//        products = state.products,
//        showOnlyFavorites = state.showOnlyFavorites,
//        isLoading = state.isLoading,
//        errorMessage = state.errorMessage,
//        onShowOnlyFavoritesChange = { value ->
//            onEvent(
//                MviEvent.ShowOnlyFavoritesChanged(value),
//            )
//        },
//        onReloadClick = {
//            onEvent(
//                MviEvent.LoadProducts(
//                    page = 1,
//                ),
//            )
//        },
//        onErrorClick = {
//            onEvent(MviEvent.LoadWithErrorClicked)
//        },
//        onLoadNextPage = {
//            onEvent(
//                MviEvent.LoadProducts(
//                    page = state.currentPage + 1,
//                ),
//            )
//        },
//        onProductClick = { productId ->
//            onEvent(
//                MviEvent.ProductClicked(productId),
//            )
//        },
//        onFavoriteClick = { productId ->
//            onEvent(
//                MviEvent.FavoriteClicked(productId),
//            )
//        },
//    )
//}



//
//@Composable
//fun MviContent(
//    state: MviState,
//    onEvent: (MviEvent) -> Unit,
//) {
//    ProductsDemoContent(
//        title = "MVI example",
//        description = "Screen sends Events, Reducer changes State, Actor executes Commands.",
//        products = state.products,
//        showOnlyFavorites = state.showOnlyFavorites,
//        isLoading = state.isLoading,
//        errorMessage = state.errorMessage,
//        onShowOnlyFavoritesChange = { value ->
//            onEvent(
//                MviEvent.ShowOnlyFavoritesChanged(value),
//            )
//        },
//        onReloadClick = {
//            onEvent(MviEvent.ReloadClicked)
//        },
//        onErrorClick = {
//            onEvent(MviEvent.LoadWithErrorClicked)
//        },
//        onLoadNextPage = {
//            onEvent(MviEvent.LoadNextPage)
//        },
//        onProductClick = { productId ->
//            onEvent(
//                MviEvent.ProductClicked(productId),
//            )
//        },
//        onFavoriteClick = { productId ->
//            onEvent(
//                MviEvent.FavoriteClicked(productId),
//            )
//        },
//    )
//}

//@Composable
//fun MviContent(
//    state: MviState,
//    onEvent: (MviEvent) -> Unit,
//) {
//    when {
//        state.isLoading -> {
//            LoadingView()
//        }
//
//        state.errorMessage != null -> {
//            ErrorContent(
//                message = state.errorMessage,
//                onRetryClick = {
//                    onEvent(MviEvent.ReloadClicked)
//                },
//            )
////            Column(
////                modifier = Modifier
////                    .statusBarsPadding()
////                    .padding(16.dp),
////            ) {
////                ErrorView(message = state.errorMessage)
////
////                Button(
////                    modifier = Modifier.padding(top = 16.dp),
////                    onClick = {
////                        onEvent(MviEvent.ReloadClicked)
////                    },
////                ) {
////                    Text(text = "Retry")
////                }
////            }
//        }
//
//        else -> {
//            ProductsDemoContent(
//                title = "MVI example",
//                description = "Screen sends Events, Reducer changes State, Actor executes Commands.",
//                products = state.products,
//                showOnlyFavorites = state.showOnlyFavorites,
//                onShowOnlyFavoritesChange = { value ->
//                    onEvent(MviEvent.ShowOnlyFavoritesChanged(value))
//                },
//                onReloadClick = {
//                    onEvent(MviEvent.ReloadClicked)
//                },
//                onErrorClick = {
//                    onEvent(MviEvent.LoadWithErrorClicked)
//                },
//                onProductClick = { productId ->
//                    onEvent(MviEvent.ProductClicked(productId))
//                },
//                onFavoriteClick = { productId ->
//                    onEvent(MviEvent.FavoriteClicked(productId))
//                },
//            )
//        }
//    }
//}
