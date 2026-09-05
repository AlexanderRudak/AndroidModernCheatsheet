package dev.rudak.androidcheatsheet.feature.mvi.presentation.actor

import dev.rudak.androidcheatsheet.domain.model.shop.Category
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import dev.rudak.androidcheatsheet.domain.usecase.preferences.SetShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsWithErrorUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ToggleFavoriteUseCase
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import io.mockk.mockk
import kotlinx.coroutines.test.runTest


class MviActorTest {

    private val getProductsUseCase = mockk<GetProductsUseCase>()
    private val getProductsWithErrorUseCase = mockk<GetProductsWithErrorUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
    private val setShowOnlyFavoritesUseCase = mockk<SetShowOnlyFavoritesUseCase>()

    private val actor = MviActor(
        getProductsUseCase = getProductsUseCase,
        getProductsWithErrorUseCase = getProductsWithErrorUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        setShowOnlyFavoritesUseCase = setShowOnlyFavoritesUseCase,
    )

    @Test
    fun `GIVEN LoadProducts command WHEN execute THEN return ProductsLoaded event`() = runTest {

        val page = 1
        val pageSize = 20

        val products = listOf(
            Product(
                id = 1L,
                title = "Phone",
                description = "Description",
                price = 999.0,
//                category = Category(
//                    id = 1L,
//                    name = "Smartphones",
//                ),
                isFavorite = false,
            )
        )

        val productsPage = ProductsPage(
            products = products,
            currentPage = 1,
            totalPages = 5,
        )

        coEvery {
            getProductsUseCase(
                page = page,
                pageSize = pageSize,
            )
        } returns productsPage

        val event = actor.execute(
            MviCommand.LoadProducts(
                page = page,
                pageSize = pageSize,
            )
        )

        assertTrue(event is MviEvent.ProductsLoaded)

        val loadedEvent = event as MviEvent.ProductsLoaded

        assertEquals(1, loadedEvent.products.size)
        assertEquals(1L, loadedEvent.products.first().id)
        assertEquals(1, loadedEvent.currentPage)
        assertEquals(5, loadedEvent.totalPages)

        coVerify(exactly = 1) {
            getProductsUseCase(
                page = page,
                pageSize = pageSize,
            )
        }
    }
}