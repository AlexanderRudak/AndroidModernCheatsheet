package dev.rudak.androidcheatsheet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.rudak.androidcheatsheet.data.local.entity.RecentlyViewedProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecentlyViewedProductDao {

    @Query("SELECT productId FROM recently_viewed_products ORDER BY viewedAt DESC")
    abstract fun observeProductIds(): Flow<List<Long>>

    @Query("DELETE FROM recently_viewed_products")
    abstract suspend fun clear()

    @Query("SELECT MAX(viewedAt) FROM recently_viewed_products")
    protected abstract suspend fun getLatestViewedAt(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insert(entity: RecentlyViewedProductEntity)

    @Query(
        "DELETE FROM recently_viewed_products WHERE productId NOT IN (" +
            "SELECT productId FROM recently_viewed_products ORDER BY viewedAt DESC LIMIT :limit)"
    )
    protected abstract suspend fun trimToLimit(limit: Int)

    /**
     * Атомарно записывает просмотр. viewedAt всегда строго больше всех существующих,
     * поэтому порядок однозначен даже при одинаковом или откатившемся системном времени.
     */
    @Transaction
    open suspend fun record(productId: Long, nowMillis: Long, limit: Int) {
        val latestViewedAt = getLatestViewedAt()
        val viewedAt = if (latestViewedAt == null) {
            nowMillis
        } else {
            maxOf(nowMillis, latestViewedAt + 1)
        }

        insert(RecentlyViewedProductEntity(productId = productId, viewedAt = viewedAt))
        trimToLimit(limit)
    }
}
