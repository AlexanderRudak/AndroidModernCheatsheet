package dev.rudak.androidcheatsheet.feature.mvi.presentation.actor

//import dev.rudak.androidcheatsheet.core.common.coroutine.runSuspendCatching
import dev.rudak.androidcheatsheet.core.common.result.runSuspendCatching
import dev.rudak.androidcheatsheet.domain.usecase.preferences.SetShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsWithErrorUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ToggleFavoriteUseCase
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.mapper.toUiModel

class MviActor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsWithErrorUseCase: GetProductsWithErrorUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val setShowOnlyFavoritesUseCase: SetShowOnlyFavoritesUseCase,
) {

    suspend fun execute(command: MviCommand): MviEvent {
        return when (command) {
            is MviCommand.LoadProducts -> {
                loadProducts(
                    page = command.page,
                    pageSize = command.pageSize,
                )
            }

            is MviCommand.LoadProductsWithError -> {
                loadProductsWithError(
                    page = command.page,
                    pageSize = command.pageSize,
                )
            }

            is MviCommand.ToggleFavorite -> {
                toggleFavorite(command.productId)
            }

            is MviCommand.SetShowOnlyFavorites -> {
                setShowOnlyFavorites(command.value)
            }
        }
    }

    private suspend fun loadProducts(
        page: Int,
        pageSize: Int,
    ): MviEvent {
        return runSuspendCatching {
            getProductsUseCase(
                page = page,
                pageSize = pageSize,
            )
        }.fold(
            onSuccess = { productsPage ->
                MviEvent.ProductsLoaded(
                    products = productsPage.products.map { product ->
                        product.toUiModel()
                    },
                    currentPage = productsPage.currentPage,
                    totalPages = productsPage.totalPages,
                )
            },
            onFailure = { throwable ->
                MviEvent.ProductsLoadingFailed(
                    message = throwable.message ?: "Unknown error",
                )
            },
        )
    }

    private suspend fun loadProductsWithError(
        page: Int,
        pageSize: Int,
    ): MviEvent {
        return runSuspendCatching {
            getProductsWithErrorUseCase(
                page = page,
                pageSize = pageSize,
            )
        }.fold(
            onSuccess = { productsPage ->
                MviEvent.ProductsLoaded(
                    products = productsPage.products.map { product ->
                        product.toUiModel()
                    },
                    currentPage = productsPage.currentPage,
                    totalPages = productsPage.totalPages,
                )
            },
            onFailure = { throwable ->
                MviEvent.ProductsLoadingFailed(
                    message = throwable.message ?: "Unknown error",
                )
            },
        )
    }

    private suspend fun toggleFavorite(
        productId: Long,
    ): MviEvent {
        return runSuspendCatching {
            toggleFavoriteUseCase(productId)
        }.fold(
            onSuccess = {
                MviEvent.FavoriteUpdated(productId)
            },
            onFailure = { throwable ->
                MviEvent.ProductsLoadingFailed(
                    message = throwable.message ?: "Unknown error",
                )
            },
        )
    }

    private suspend fun setShowOnlyFavorites(
        value: Boolean,
    ): MviEvent {
        return runSuspendCatching {
            setShowOnlyFavoritesUseCase(value)
        }.fold(
            onSuccess = {
                MviEvent.ShowOnlyFavoritesSaved(value)
            },
            onFailure = { throwable ->
                MviEvent.ProductsLoadingFailed(
                    message = throwable.message ?: "Unknown error",
                )
            },
        )
    }
}

//
//class MviActor(
//    private val getProductsUseCase: GetProductsUseCase,
//    private val getProductsWithErrorUseCase: GetProductsWithErrorUseCase,
//    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
//    private val setShowOnlyFavoritesUseCase: SetShowOnlyFavoritesUseCase,
//) {
//
//    suspend fun execute(command: MviCommand): MviEvent {
//        return when (command) {
//            MviCommand.LoadProducts -> loadProducts()
//
//            MviCommand.LoadProductsWithError -> loadProductsWithError()
//
//            is MviCommand.ToggleFavorite -> toggleFavorite(command.productId)
//
//            is MviCommand.SetShowOnlyFavorites -> setShowOnlyFavorites(command.value)
//        }
//    }
//
//    private suspend fun loadProducts(): MviEvent {
//        return runSuspendCatching {
//            getProductsUseCase()
//                .map { product -> product.toUiModel() }
//        }.fold(
//            onSuccess = { products ->
//                MviEvent.ProductsLoaded(products)
//            },
//            onFailure = { throwable ->
//                MviEvent.ProductsLoadingFailed(
//                    message = throwable.message ?: "Unknown error",
//                )
//            },
//        )
//    }
//
//    private suspend fun loadProductsWithError(): MviEvent {
//        return runSuspendCatching {
//            getProductsWithErrorUseCase()
//                .map { product -> product.toUiModel() }
//        }.fold(
//            onSuccess = { products ->
//                MviEvent.ProductsLoaded(products)
//            },
//            onFailure = { throwable ->
//                MviEvent.ProductsLoadingFailed(
//                    message = throwable.message ?: "Unknown error",
//                )
//            },
//        )
//    }
//
//    private suspend fun toggleFavorite(productId: Long): MviEvent {
//        return runSuspendCatching {
//            toggleFavoriteUseCase(productId)
//        }.fold(
//            onSuccess = {
//                MviEvent.FavoriteUpdated(productId)
//            },
//            onFailure = { throwable ->
//                MviEvent.ProductsLoadingFailed(
//                    message = throwable.message ?: "Unknown error",
//                )
//            },
//        )
//    }
//
//    private suspend fun setShowOnlyFavorites(value: Boolean): MviEvent {
//        return runSuspendCatching {
//            setShowOnlyFavoritesUseCase(value)
//        }.fold(
//            onSuccess = {
//                MviEvent.ShowOnlyFavoritesSaved(value)
//            },
//            onFailure = { throwable ->
//                MviEvent.ProductsLoadingFailed(
//                    message = throwable.message ?: "Unknown error",
//                )
//            },
//        )
//    }
//
//}