package dev.rudak.androidcheatsheet.feature.mvi.presentation.actor

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
import org.junit.Test
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class MviActionActorTest {

    private val getProductsUseCase = mockk<GetProductsUseCase>()
    private val getProductsWithErrorUseCase = mockk<GetProductsWithErrorUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
    private val setShowOnlyFavoritesUseCase = mockk<SetShowOnlyFavoritesUseCase>()

    private val actor = MviActionActor(
        getProductsUseCase = getProductsUseCase,
        getProductsWithErrorUseCase = getProductsWithErrorUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        setShowOnlyFavoritesUseCase = setShowOnlyFavoritesUseCase,
    )

    @Test
    fun `GIVEN LoadProducts command WHEN processed THEN emit ProductsLoaded event`() = runTest {
        val page = 1
        val pageSize = 20

        val productsPage = ProductsPage(
            products = listOf(
                Product(
                    id = 1L,
                    title = "Phone",
                    description = "Description",
                    price = 999.0,
                    isFavorite = false,
                )
            ),
            currentPage = 1,
            totalPages = 5,
        )

        coEvery {
            getProductsUseCase(page = page, pageSize = pageSize)
        } returns productsPage

        val event = actor.process(
            flowOf(MviCommand.LoadProducts(page = page, pageSize = pageSize))
        ).single()

        assertTrue(event is MviEvent.ProductsLoaded)

        val loadedEvent = event as MviEvent.ProductsLoaded

        assertEquals(1, loadedEvent.products.size)
        assertEquals(1L, loadedEvent.products.first().id)
        assertEquals(1, loadedEvent.currentPage)
        assertEquals(5, loadedEvent.totalPages)

        coVerify(exactly = 1) {
            getProductsUseCase(page = page, pageSize = pageSize)
        }
    }

    @Test
    fun `GIVEN LoadProducts failure WHEN processed THEN emit ProductsLoadingFailed event`() = runTest {
        val page = 1
        val pageSize = 20

        coEvery {
            getProductsUseCase(page = page, pageSize = pageSize)
        } throws IllegalStateException("Network error")

        val event = actor.process(
            flowOf(MviCommand.LoadProducts(page = page, pageSize = pageSize))
        ).single()

        assertEquals(MviEvent.ProductsLoadingFailed("Network error"), event)

        coVerify(exactly = 1) {
            getProductsUseCase(page = page, pageSize = pageSize)
        }
    }

    @Test
    fun `GIVEN analytic command WHEN processed THEN emit no events`() = runTest {
        val events = actor.process(
            flowOf(MviCommand.Analytic.ProductClicked(productId = 5L))
        ).toList()

        assertTrue(events.isEmpty())
    }

}
