package dev.rudak.androidcheatsheet.feature.mvi.presentation.reducer

import dev.rudak.androidcheatsheet.feature.mvi.core.ReduceResult
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.state.MviState

class MviReducer {

    fun reduce(
        state: MviState,
        event: MviEvent,
    ): ReduceResult<MviState, MviCommand> {
        return when (event) {

            is MviEvent.LoadProducts -> {
                if (
                    state.isLoading ||
                    event.page < FIRST_PAGE ||
                    state.totalPages > 0 && event.page > state.totalPages
                ) {
                    ReduceResult(state = state)
                } else {
                    ReduceResult(
                        state = state.copy(
                            isLoading = true,
                            errorMessage = null,
                        ),
                        command = MviCommand.LoadProducts(
                            page = event.page,
                            pageSize = PAGE_SIZE,
                        ),
                    )
                }
            }

            MviEvent.LoadWithErrorClicked -> {
                ReduceResult(
                    state = state.copy(
                        isLoading = true,
                        errorMessage = null,
                    ),
                    command = MviCommand.LoadProductsWithError(
                        page = FIRST_PAGE,
                        pageSize = PAGE_SIZE,
                    ),
                )
            }

            is MviEvent.ProductsLoaded -> {
                val updatedProducts =
                    if (event.currentPage == FIRST_PAGE) {
                        event.products
                    } else {
                        state.products + event.products
                    }

                ReduceResult(
                    state = state.copy(
                        products = updatedProducts,
                        currentPage = event.currentPage,
                        totalPages = event.totalPages,
                        isLoading = false,
                        errorMessage = null,
                    ),
                )
            }

            is MviEvent.ProductsLoadingFailed -> {
                ReduceResult(
                    state = state.copy(
                        isLoading = false,
                        errorMessage = event.message,
                    ),
                )
            }

            is MviEvent.FavoriteClicked -> {
                ReduceResult(
                    state = state,
                    command = MviCommand.ToggleFavorite(
                        productId = event.productId,
                    ),
                )
            }

            is MviEvent.FavoriteUpdated -> {
                ReduceResult(
                    state = state.copy(
                        products = state.products.map { product ->
                            if (product.id == event.productId) {
                                product.copy(
                                    isFavorite = !product.isFavorite,
                                )
                            } else {
                                product
                            }
                        },
                    ),
                )
            }

            is MviEvent.ShowOnlyFavoritesChanged -> {
                ReduceResult(
                    state = state.copy(
                        showOnlyFavorites = event.value,
                    ),
                    command = MviCommand.SetShowOnlyFavorites(
                        value = event.value,
                    ),
                )
            }

            is MviEvent.ProductClicked -> {
                ReduceResult(state = state)
            }

            is MviEvent.ShowOnlyFavoritesSaved -> {
                ReduceResult(state = state)
            }
        }
    }

    private companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 10
    }
}

//
//class MviReducer {
//
//    fun reduce(
//        state: MviState,
//        event: MviEvent,
//    ): ReduceResult<MviState, MviCommand> {
//        return when (event) {
//            MviEvent.LoadProducts,
//            MviEvent.ReloadClicked -> {
//                ReduceResult(
//                    state = state.copy(
//                        isLoading = true,
//                        errorMessage = null,
//                    ),
//                    command = MviCommand.LoadProducts,
//                )
//            }
//
//            MviEvent.LoadWithErrorClicked -> {
//                ReduceResult(
//                    state = state.copy(
//                        isLoading = true,
//                        errorMessage = null,
//                    ),
//                    command = MviCommand.LoadProductsWithError,
//                )
//            }
//
//            is MviEvent.ProductsLoaded -> {
//                ReduceResult(
//                    state = state.copy(
//                        isLoading = false,
//                        products = event.products,
//                        errorMessage = null,
//                    ),
//                )
//            }
//
//            is MviEvent.ProductsLoadingFailed -> {
//                ReduceResult(
//                    state = state.copy(
//                        isLoading = false,
//                        errorMessage = event.message,
//                    ),
//                )
//            }
//
//            is MviEvent.FavoriteClicked -> {
//                ReduceResult(
//                    state = state,
//                    command = MviCommand.ToggleFavorite(event.productId),
//                )
//            }
//
//            is MviEvent.FavoriteUpdated -> {
//                ReduceResult(
//                    state = state.copy(
//                        products = state.products.map { product ->
//                            if (product.id == event.productId) {
//                                product.copy(isFavorite = !product.isFavorite)
//                            } else {
//                                product
//                            }
//                        },
//                    ),
//                )
//            }
//
//            is MviEvent.ShowOnlyFavoritesChanged -> {
//                ReduceResult(
//                    state = state.copy(
//                        showOnlyFavorites = event.value,
//                    ),
//                    command = MviCommand.SetShowOnlyFavorites(event.value),
//                )
//            }
//
//            is MviEvent.ProductClicked -> {
//                ReduceResult(state = state)
//            }
//
//            is MviEvent.ShowOnlyFavoritesSaved -> {
//                ReduceResult(state = state)
//            }
//        }
//    }
//}
//
////class MviReducer {
////
////    fun reduce(
////        state: MviState,
////        event: MviEvent,
////    ): MviState {
////        return when (event) {
////            MviEvent.LoadProducts,
////            MviEvent.ReloadClicked,
////            MviEvent.LoadWithErrorClicked -> {
////                state.copy(
////                    isLoading = true,
////                    errorMessage = null,
////                )
////            }
////
////            is MviEvent.ShowOnlyFavoritesChanged -> {
////                state.copy(
////                    showOnlyFavorites = event.value,
////                )
////            }
////
////            is MviEvent.FavoriteClicked -> {
////                state.copy(
////                    products = state.products.map { product ->
////                        if (product.id == event.productId) {
////                            product.copy(isFavorite = !product.isFavorite)
////                        } else {
////                            product
////                        }
////                    },
////                )
////            }
////
////            is MviEvent.ProductClicked -> {
////                state
////            }
////        }
////    }
////
////    fun productsLoaded(
////        state: MviState,
////        products: List<ProductUiModel>,
////    ): MviState {
////        return state.copy(
////            isLoading = false,
////            products = products,
////            errorMessage = null,
////        )
////    }
////
////    fun error(
////        state: MviState,
////        message: String,
////    ): MviState {
////        return state.copy(
////            isLoading = false,
////            errorMessage = message,
////        )
////    }
////}