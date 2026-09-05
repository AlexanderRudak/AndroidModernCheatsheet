package dev.rudak.androidcheatsheet.data.repository.api

import dev.rudak.androidcheatsheet.data.repository.remote.ProductDto
import kotlinx.coroutines.delay
import dev.rudak.androidcheatsheet.data.repository.remote.ProductJsonLoader
import dev.rudak.androidcheatsheet.data.repository.remote.ProductsPageDto


class FakeProductApi(
    private val productJsonLoader: ProductJsonLoader,
) : ProductApi {

    private val products: List<ProductDto> by lazy {
        productJsonLoader.loadProducts()
    }

//    override suspend fun getProducts(): List<ProductDto> {
//        delay(1000)
//        return products
//    }
    override suspend fun getProducts(
        page: Int,
        pageSize: Int,
    ): ProductsPageDto {
        delay(1000)

        require(page > 0) {
            "Page must be greater than 0"
        }
        require(pageSize > 0) {
            "Page size must be greater than 0"
        }

        val totalPages = (products.size + pageSize - 1) / pageSize
        val fromIndex = (page - 1) * pageSize

        val pageProducts =
            if (fromIndex >= products.size) {
                emptyList()
            } else {
                val toIndex = minOf(
                    fromIndex + pageSize,
                    products.size,
                )

                products.subList(fromIndex, toIndex)
            }

        return ProductsPageDto(
            products = pageProducts,
            currentPage = page,
            totalPages = totalPages,
        )
    }

//    override suspend fun getProductsWithError(): List<ProductDto> {
//        delay(1000)
//        error("Network error: failed to load products")
//    }
    override suspend fun getProductsWithError(
        page: Int,
        pageSize: Int,
    ): ProductsPageDto {
        delay(1000)
        error("Network error: failed to load products")
    }

    override suspend fun getProductById(id: Long): ProductDto? {
        delay(500)

        return products.firstOrNull { product ->
            product.id == id
        }
    }

    override suspend fun getRecommendedProducts(
        productId: Long,
    ): List<ProductDto> {
        delay(1000)

        return products
            .filter { product -> product.id != productId }
            .take(3)
    }
}

//class FakeProductApi : ProductApi {
//
//    private val products = listOf(
//        ProductDto(
//            id = 1L,
//            title = "Android Phone",
//            description = "Simple product for MVVM example.",
//            price = 399.99,
//            categoryId = 1L,
//            categoryName = "Electronics",
//        ),
//        ProductDto(
//            id = 2L,
//            title = "Wireless Headphones",
//            description = "Used later for list, details and state examples.",
//            price = 89.99,
//            categoryId = 1L,
//            categoryName = "Electronics",
//        ),
//        ProductDto(
//            id = 3L,
//            title = "USB-C Charger",
//            description = "Small item for repository and use case examples.",
//            price = 24.99,
//            categoryId = 1L,
//            categoryName = "Electronics",
//        ),
//    )
//
//    override suspend fun getProducts(): List<ProductDto> {
//        delay(1000)
//        return products
//    }
//
//    override suspend fun getProductsWithError(): List<ProductDto> {
//        delay(1000)
//        error("Network error: failed to load products")
//    }
//
//    override suspend fun getProductById(id: Long): ProductDto? {
//        delay(500)
//        return products.firstOrNull { product -> product.id == id }
//    }
//
//    //В fake-реализации вернуть несколько товаров, кроме текущего
//    override suspend fun getRecommendedProducts(productId: Long): List<ProductDto> {
//        delay(1000)
//
//        return products
//            .filter { it.id != productId }
//            .take(3)
//    }
//}