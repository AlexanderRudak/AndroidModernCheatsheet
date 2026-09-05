package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

class ToggleFavoriteUseCase(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(productId: Long) {
        productRepository.toggleFavorite(productId)
    }
}