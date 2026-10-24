package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

class ClearRecentlyViewedProductsUseCase(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke() {
        productRepository.clearRecentlyViewedProducts()
    }
}
