package dev.rudak.androidcheatsheet.domain.usecase


import dev.rudak.androidcheatsheet.domain.model.shop.Category
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductByIdUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetProductByIdUseCaseTest {

    private val productRepository = mockk<ProductRepository>()
    private val useCase = GetProductByIdUseCase(productRepository)

    @Test
    fun `GIVEN existing product id WHEN invoke THEN return product`() = runTest {
        // GIVEN
        val expectedProduct = Product(
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

        coEvery {
            productRepository.getProductById(1L)
        } returns expectedProduct

        // WHEN
        val actualProduct = useCase(1L)

        // THEN
        assertEquals(expectedProduct, actualProduct)

        coVerify(exactly = 1) {
            productRepository.getProductById(1L)
        }
    }

    @Test
    fun `GIVEN unknown product id WHEN invoke THEN return null`() = runTest {
        // GIVEN
        coEvery {
            productRepository.getProductById(999L)
        } returns null

        // WHEN
        val actualProduct = useCase(999L)

        // THEN
        assertNull(actualProduct)

        coVerify(exactly = 1) {
            productRepository.getProductById(999L)
        }
    }
}