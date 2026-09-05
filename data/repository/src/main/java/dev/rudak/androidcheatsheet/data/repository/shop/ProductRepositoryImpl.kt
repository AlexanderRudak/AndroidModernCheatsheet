package dev.rudak.androidcheatsheet.data.repository.shop

import dev.rudak.androidcheatsheet.data.local.datasource.FavoriteProductLocalDataSource
import dev.rudak.androidcheatsheet.data.repository.api.ProductApi
import dev.rudak.androidcheatsheet.data.repository.mapper.toDomain
import dev.rudak.androidcheatsheet.domain.model.shop.Category
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.model.shop.ProductDetailsData
import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val favoriteProductLocalDataSource: FavoriteProductLocalDataSource,
) : ProductRepository {

//    override suspend fun getProducts(): List<Product> {
//        val favoriteIds = favoriteProductLocalDataSource.getFavoriteIds()
//
//        return productApi.getProducts()
//            .map { productDto ->
//                productDto.toDomain(
//                    isFavorite = productDto.id in favoriteIds
//                )
//            }
//    }
    override suspend fun getProducts(
        page: Int,
        pageSize: Int,
    ): ProductsPage {
        val favoriteIds = favoriteProductLocalDataSource.getFavoriteIds()

        val response = productApi.getProducts(
            page = page,
            pageSize = pageSize,
        )

        return ProductsPage(
            products = response.products.map { dto ->
                dto.toDomain(
                    isFavorite = dto.id in favoriteIds,
                )
            },
            currentPage = response.currentPage,
            totalPages = response.totalPages,
        )
    }

//    override suspend fun getProductsWithError(): List<Product> {
//        val favoriteIds = favoriteProductLocalDataSource.getFavoriteIds()
//        return productApi.getProductsWithError()
//            .map { dto ->
//                dto.toDomain(
//                    isFavorite = dto.id in favoriteIds
//                )
//            }
//    }
    override suspend fun getProductsWithError(
        page: Int,
        pageSize: Int,
    ): ProductsPage {
        val favoriteIds = favoriteProductLocalDataSource.getFavoriteIds()

        val response = productApi.getProductsWithError(
            page = page,
            pageSize = pageSize,
        )

        return ProductsPage(
            products = response.products.map { dto ->
                dto.toDomain(
                    isFavorite = dto.id in favoriteIds,
                )
            },
            currentPage = response.currentPage,
            totalPages = response.totalPages,
        )
    }

    override suspend fun getProductById(id: Long): Product? {
        return productApi.getProductById(id)
            ?.toDomain(
                isFavorite = favoriteProductLocalDataSource.isFavorite(id)
            )
    }

    override suspend fun getRecommendedProducts(productId: Long): List<Product> {
        val favoriteIds = favoriteProductLocalDataSource.getFavoriteIds()

        return productApi.getRecommendedProducts(productId)
            .map { dto ->
                dto.toDomain(
                    isFavorite = dto.id in favoriteIds,
                )
            }
    }

    override suspend fun toggleFavorite(productId: Long) {
        favoriteProductLocalDataSource.toggleFavorite(productId)
    }

    override suspend fun getProductDetailsData(
        productId: Long,
    ): ProductDetailsData = coroutineScope {

        val productDeferred = async {
            productApi.getProductById(productId)
        }

        val recommendedDeferred = async {
            productApi.getRecommendedProducts(productId)
        }

        val favoriteIdsDeferred = async {
            favoriteProductLocalDataSource.getFavoriteIds()
        }

        val productDto = productDeferred.await()
        val recommendedDtos = recommendedDeferred.await()
        val favoriteIds = favoriteIdsDeferred.await()

        ProductDetailsData(
            product = productDto?.toDomain(
                isFavorite = productId in favoriteIds,
            ),
            recommendedProducts = recommendedDtos.map { dto ->
                dto.toDomain(
                    isFavorite = dto.id in favoriteIds,
                )
            },
        )
    }



//    override suspend fun getProducts(): List<Product> {
//        delay(2000)
//        return products
//    }
//
//    override suspend fun getProductById(id: Long): Product? {
//        return products.firstOrNull { product -> product.id == id }
//    }
}