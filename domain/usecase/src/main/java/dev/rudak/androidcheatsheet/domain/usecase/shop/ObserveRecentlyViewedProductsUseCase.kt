package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveRecentlyViewedProductsUseCase(
    private val productRepository: ProductRepository,
) {

    operator fun invoke(): Flow<List<Product>> {
        return productRepository.observeRecentlyViewedProducts()
    }
}
