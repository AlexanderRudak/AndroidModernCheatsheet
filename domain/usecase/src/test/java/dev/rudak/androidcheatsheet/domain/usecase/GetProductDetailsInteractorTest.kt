package dev.rudak.androidcheatsheet.domain.usecase

import dev.rudak.androidcheatsheet.domain.model.shop.Category
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductByIdUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductDetailsInteractor
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetRecommendedProductsUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetProductDetailsInteractorTest {

    private val getProductByIdUseCase = mockk<GetProductByIdUseCase>()
    private val getRecommendedProductsUseCase = mockk<GetRecommendedProductsUseCase>()

    private val interactor = GetProductDetailsInteractor(
        getProductByIdUseCase = getProductByIdUseCase,
        getRecommendedProductsUseCase = getRecommendedProductsUseCase,
    )

    @Test
    fun `GIVEN product id WHEN invoke THEN return product details data`() = runTest {
        val product = Product(
            id = 1L,
            title = "iPhone 16",
            description = "Apple smartphone",
            price = 999.0,
            category = Category(
                id = 10L,
                name = "Smartphones",
            ),
            isFavorite = false,
        )

        val recommendedProducts = listOf(
            Product(
                id = 2L,
                title = "Samsung Galaxy",
                description = "Android smartphone",
                price = 799.0,
                category = Category(
                    id = 10L,
                    name = "Smartphones",
                ),
                isFavorite = false,
            )
        )

        coEvery {
            getProductByIdUseCase(1L)
        } returns product

        coEvery {
            getRecommendedProductsUseCase(1L)
        } returns recommendedProducts

        val actualResult = interactor(1L)

        assertEquals(product, actualResult.product)
        assertEquals(recommendedProducts, actualResult.recommendedProducts)

        coVerify(exactly = 1) {
            getProductByIdUseCase(1L)
        }

        coVerify(exactly = 1) {
            getRecommendedProductsUseCase(1L)
        }
    }
}