package dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel

import dev.rudak.androidcheatsheet.core.analytics.AnalyticEvent
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEventKey
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEventName
import dev.rudak.androidcheatsheet.core.analytics.AnalyticSenderController
import dev.rudak.androidcheatsheet.core.analytics.firebase.AnalyticEventValue
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import dev.rudak.androidcheatsheet.domain.usecase.preferences.ObserveShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.preferences.SetShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsWithErrorUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MvvmViewModelTest {

    private val getProductsUseCase = mockk<GetProductsUseCase>()
    private val getProductsWithErrorUseCase = mockk<GetProductsWithErrorUseCase>()
    private val observeShowOnlyFavoritesUseCase = mockk<ObserveShowOnlyFavoritesUseCase>()
    private val setShowOnlyFavoritesUseCase = mockk<SetShowOnlyFavoritesUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
    private val analyticController = mockk<AnalyticSenderController>(relaxed = true)

    private val product = Product(
        id = 1L,
        title = "Phone",
        description = "Description",
        price = 999.0,
        isFavorite = false,
    )

    private fun createViewModel() = MvvmViewModel(
        getProductsUseCase = getProductsUseCase,
        getProductsWithErrorUseCase = getProductsWithErrorUseCase,
        observeShowOnlyFavoritesUseCase = observeShowOnlyFavoritesUseCase,
        setShowOnlyFavoritesUseCase = setShowOnlyFavoritesUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        analyticController = analyticController,
    )

    private fun stubProducts(products: List<Product> = listOf(product)) {
        every { observeShowOnlyFavoritesUseCase() } returns flowOf(false)
        coEvery { getProductsUseCase(page = 1, pageSize = 10) } returns ProductsPage(
            products = products,
            currentPage = 1,
            totalPages = 2,
        )
    }

    @Test
    fun `GIVEN products WHEN ViewModel initializes THEN state contains products`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            stubProducts()
            val viewModel = createViewModel()

            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertNull(viewModel.uiState.value.errorMessage)
            assertEquals(1, viewModel.uiState.value.products.size)
            assertEquals(1L, viewModel.uiState.value.products.single().id)
            assertEquals(1, viewModel.uiState.value.currentPage)
            assertEquals(2, viewModel.uiState.value.totalPages)
            coVerify(exactly = 1) { getProductsUseCase(page = 1, pageSize = 10) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `GIVEN loading failure WHEN ViewModel initializes THEN state contains error`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            every { observeShowOnlyFavoritesUseCase() } returns flowOf(false)
            coEvery { getProductsUseCase(page = 1, pageSize = 10) } throws IllegalStateException("Network error")
            val viewModel = createViewModel()

            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("Network error", viewModel.uiState.value.errorMessage)
            assertTrue(viewModel.uiState.value.products.isEmpty())
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `GIVEN product WHEN favorite toggled THEN state and analytics are updated`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            stubProducts()
            coEvery { toggleFavoriteUseCase(1L) } returns Unit
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.toggleFavorite(1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.products.single().isFavorite)
            coVerify(exactly = 1) { toggleFavoriteUseCase(1L) }
            verify(exactly = 1) {
                analyticController.sendEvent(
                    AnalyticEvent(
                        name = AnalyticEventName.FAVORITE_CLICKED,
                        params = mapOf(
                            AnalyticEventKey.PRODUCT_ID to 1L,
                            AnalyticEventKey.ACTION to AnalyticEventValue.ADD,
                        ),
                    ),
                )
            }
        } finally {
            Dispatchers.resetMain()
        }
    }
}
