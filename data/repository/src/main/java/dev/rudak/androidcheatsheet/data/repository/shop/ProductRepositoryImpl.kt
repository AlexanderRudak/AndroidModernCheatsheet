package dev.rudak.androidcheatsheet.data.repository.shop

import dev.rudak.androidcheatsheet.data.local.datasource.FavoriteProductLocalDataSource
import dev.rudak.androidcheatsheet.data.local.datasource.RecentlyViewedLocalDataSource
import dev.rudak.androidcheatsheet.data.repository.api.ProductApi
import dev.rudak.androidcheatsheet.data.repository.mapper.toDomain
import dev.rudak.androidcheatsheet.data.repository.remote.ProductDto
import dev.rudak.androidcheatsheet.domain.model.shop.Category
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.model.shop.ProductDetailsData
import dev.rudak.androidcheatsheet.domain.model.shop.ProductsPage
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val favoriteProductLocalDataSource: FavoriteProductLocalDataSource,
    private val recentlyViewedLocalDataSource: RecentlyViewedLocalDataSource,
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

    override fun observeFavoriteIds(): Flow<List<Long>> {
        return favoriteProductLocalDataSource.observeFavoriteIds()
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

    override suspend fun recordProductView(productId: Long) {
        recentlyViewedLocalDataSource.recordView(productId)
    }

    override suspend fun clearRecentlyViewedProducts() {
        recentlyViewedLocalDataSource.clearHistory()
    }

    override suspend fun getProductsByIds(ids: List<Long>): List<Product> {
        val products = fetchProductsInOrder(ids)

        if (products.isEmpty()) {
            return emptyList()
        }

        val favoriteIds = favoriteProductLocalDataSource.getFavoriteIds()

        return products.map { dto ->
            dto.toDomain(
                isFavorite = dto.id in favoriteIds,
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeRecentlyViewedProducts(): Flow<List<Product>> {
        return recentlyViewedLocalDataSource.observeProductIds()
            .distinctUntilChanged()
            .mapLatest { ids -> fetchProductsInOrder(ids) }
            .combine(favoriteProductLocalDataSource.observeFavoriteIds()) { products, favoriteIds ->
                products.map { dto ->
                    dto.toDomain(
                        isFavorite = dto.id in favoriteIds,
                    )
                }
            }
    }

    /**
     * Порядок результата строго соответствует ids, независимо от порядка ответа API.
     * Отсутствующие товары пропускаются, дубли исключаются. При пустом списке API не вызывается.
     */
    private suspend fun fetchProductsInOrder(ids: List<Long>): List<ProductDto> {
        val distinctIds = ids.distinct()

        if (distinctIds.isEmpty()) {
            return emptyList()
        }

        val productsById = productApi.getProductsByIds(distinctIds).associateBy { dto -> dto.id }

        return distinctIds.mapNotNull { id -> productsById[id] }
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