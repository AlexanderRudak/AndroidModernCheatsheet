package dev.rudak.androidcheatsheet.data.repository.api

import dev.rudak.androidcheatsheet.data.repository.remote.ProductDto
import dev.rudak.androidcheatsheet.data.repository.remote.ProductsPageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitProductService {

//    @GET("products")
//    suspend fun getProducts(): List<ProductDto>
    @GET("products")
    suspend fun getProducts(
    @Query("page") page: Int,
    @Query("pageSize") pageSize: Int,
    ): ProductsPageDto

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Long,
    ): ProductDto?
}