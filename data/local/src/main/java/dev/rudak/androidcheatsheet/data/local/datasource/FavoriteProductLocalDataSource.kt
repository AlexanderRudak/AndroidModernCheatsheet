package dev.rudak.androidcheatsheet.data.local.datasource

import dev.rudak.androidcheatsheet.data.local.dao.FavoriteProductDao
import dev.rudak.androidcheatsheet.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

class FavoriteProductLocalDataSource(
    private val favoriteProductDao: FavoriteProductDao,
) {

    fun observeFavoriteIds(): Flow<List<Long>> {
        return favoriteProductDao.observeFavoriteIds()
    }

    suspend fun toggleFavorite(productId: Long) {
        val isFavorite = favoriteProductDao.isFavorite(productId)

        if (isFavorite) {
            favoriteProductDao.deleteByProductId(productId)
        } else {
            favoriteProductDao.insert(
                FavoriteProductEntity(productId = productId)
            )
        }
    }

    suspend fun getFavoriteIds(): List<Long> {
        return favoriteProductDao.getFavoriteIds()
    }

    suspend fun isFavorite(productId: Long): Boolean {
        return favoriteProductDao.isFavorite(productId)
    }
}