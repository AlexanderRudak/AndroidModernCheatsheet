package dev.rudak.androidcheatsheet.feature.mvi.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.rudak.androidcheatsheet.core.ui.components.ErrorContent
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
    onSettingsClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagedProducts = viewModel.pagedProducts.collectAsLazyPagingItems()

    BackHandler(enabled = state.errorMessage != null) {
        viewModel.onEvent(MviEvent.ErrorDismissed)
    }

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
        onSettingsClick = onSettingsClick,
    )
}

@Composable
fun MviContent(
    state: MviState,
    pagedProducts: LazyPagingItems<ProductCardUiModel>,
    onEvent: (MviEvent) -> Unit,
    onSettingsClick: () -> Unit,
) {
    when {
        state.errorMessage != null -> {
            ErrorContent(
                message = state.errorMessage,
                onRetryClick = {
                    onEvent(MviEvent.LoadProducts(page = 1))
                },
            )
        }

        else -> {
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
                onFavoriteClick = { productId, isFavorite ->
                    onEvent(
                        MviEvent.FavoriteClicked(
                            productId = productId,
                            isFavorite = isFavorite,
                        ),
                    )
                },
                onSettingsClick = onSettingsClick,
            )
        }
    }
}
