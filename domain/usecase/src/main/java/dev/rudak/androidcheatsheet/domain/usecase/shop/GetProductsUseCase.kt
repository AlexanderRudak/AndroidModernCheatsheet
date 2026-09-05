package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

//class GetProductsUseCase(
//    private val repository: ProductRepository,
//) {
//
//    suspend operator fun invoke(): List<Product> {
//        return repository.getProducts()
//    }
//}

class GetProductsUseCase(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
    ): ProductsPage {
        return productRepository.getProducts(
            page = page,
            pageSize = pageSize,
        )
    }
}