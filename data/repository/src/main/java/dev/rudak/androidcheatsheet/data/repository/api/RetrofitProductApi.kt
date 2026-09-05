package dev.rudak.androidcheatsheet.data.repository.api

import dev.rudak.androidcheatsheet.data.repository.remote.ProductDto
import dev.rudak.androidcheatsheet.data.repository.remote.ProductsPageDto
import kotlinx.coroutines.delay

class RetrofitProductApi(
    private val service: RetrofitProductService,
) : ProductApi {

//    override suspend fun getProducts(): List<ProductDto> {
//        return service.getProducts()
//    }
    override suspend fun getProducts(
        page: Int,
        pageSize: Int,
    ): ProductsPageDto {
        return service.getProducts(
            page = page,
            pageSize = pageSize,
        )
    }


//    override suspend fun getProductsWithError(): List<ProductDto> {
//        error("Network error from Retrofit API")
//    }
    override suspend fun getProductsWithError(
        page: Int,
        pageSize: Int,
    ): ProductsPageDto {
        error("Network error from Retrofit API")
    }

    override suspend fun getProductById(id: Long): ProductDto? {
        return service.getProductById(id)
    }

//    override suspend fun getRecommendedProducts(productId: Long): List<ProductDto> {
//        delay(1000)
//
//        val products = getProducts()
//
//        return products
//            .filter { product -> product.id != productId }
//            .take(3)
//    }
    override suspend fun getRecommendedProducts(
        productId: Long,
    ): List<ProductDto> {
        val products = service.getProducts(
            page = 1,
            pageSize = Int.MAX_VALUE,
        ).products

        return products
            .filter { it.id != productId }
            .take(3)
    }
}