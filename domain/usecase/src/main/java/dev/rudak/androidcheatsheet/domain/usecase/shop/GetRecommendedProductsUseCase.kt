package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

class GetRecommendedProductsUseCase(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(productId: Long): List<Product> {
        return productRepository.getRecommendedProducts(productId)
    }
}