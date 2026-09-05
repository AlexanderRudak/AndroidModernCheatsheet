package dev.rudak.androidcheatsheet.domain.usecase


import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import dev.rudak.androidcheatsheet.domain.usecase.shop.ToggleFavoriteUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val productRepository = mockk<ProductRepository>()
    private val useCase = ToggleFavoriteUseCase(productRepository)

    @Test
    fun `GIVEN product id WHEN invoke THEN toggle favorite in repository`() = runTest {
        coEvery { productRepository.toggleFavorite(PRODUCT_ID) } just Runs

        useCase(PRODUCT_ID)

        coVerify(exactly = 1) {
            productRepository.toggleFavorite(PRODUCT_ID)
        }
    }

    private companion object {
        const val PRODUCT_ID = 1L
    }
}