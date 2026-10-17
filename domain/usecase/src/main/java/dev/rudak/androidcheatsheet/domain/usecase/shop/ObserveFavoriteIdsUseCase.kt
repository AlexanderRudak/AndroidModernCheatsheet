package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteIdsUseCase(
    private val productRepository: ProductRepository,
) {

    operator fun invoke(): Flow<List<Long>> {
        return productRepository.observeFavoriteIds()
    }
}
