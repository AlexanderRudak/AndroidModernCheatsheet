package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(id: Long): Product? {
        return repository.getProductById(id)
    }
}