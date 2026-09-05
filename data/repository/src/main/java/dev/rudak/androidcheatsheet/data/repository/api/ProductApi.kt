package dev.rudak.androidcheatsheet.data.repository.api

import dev.rudak.androidcheatsheet.data.repository.remote.ProductDto
import dev.rudak.androidcheatsheet.data.repository.remote.ProductsPageDto

interface ProductApi {

//    suspend fun getProducts(): List<ProductDto>
    suspend fun getProducts(
        page: Int,
        pageSize: Int,
    ): ProductsPageDto

//    suspend fun getProductsWithError(): List<ProductDto>
    suspend fun getProductsWithError(
        page: Int,
        pageSize: Int,
    ): ProductsPageDto

    suspend fun getProductById(id: Long): ProductDto?

    suspend fun getRecommendedProducts(productId: Long): List<ProductDto>
}