package dev.rudak.androidcheatsheet.domain.repository.shop

import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.model.shop.ProductDetailsData
import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    //suspend fun getProducts(): List<Product>
    suspend fun getProducts(
        page: Int,
        pageSize: Int,
    ): ProductsPage

    //suspend fun getProductsWithError(): List<Product>
    suspend fun getProductsWithError(
        page: Int,
        pageSize: Int,
    ): ProductsPage

    suspend fun getProductById(id: Long): Product?

    suspend fun toggleFavorite(productId: Long)

    suspend fun getRecommendedProducts(productId: Long): List<Product>

    suspend fun getProductDetailsData(productId: Long): ProductDetailsData

}