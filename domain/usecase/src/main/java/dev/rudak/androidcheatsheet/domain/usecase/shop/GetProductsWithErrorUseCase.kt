package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

//class GetProductsWithErrorUseCase(
//    private val repository: ProductRepository,
//) {
//    suspend operator fun invoke() = repository.getProductsWithError()
//}

class GetProductsWithErrorUseCase(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
    ): ProductsPage {
        return productRepository.getProductsWithError(
            page = page,
            pageSize = pageSize,
        )
    }
}