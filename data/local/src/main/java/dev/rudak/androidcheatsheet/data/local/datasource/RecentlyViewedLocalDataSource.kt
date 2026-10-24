package dev.rudak.androidcheatsheet.data.local.datasource

import dev.rudak.androidcheatsheet.data.local.dao.RecentlyViewedProductDao
import kotlinx.coroutines.flow.Flow

private const val MAX_HISTORY_SIZE = 10

class RecentlyViewedLocalDataSource(
    private val recentlyViewedProductDao: RecentlyViewedProductDao,
    private val clock: () -> Long = System::currentTimeMillis,
) {

    fun observeProductIds(): Flow<List<Long>> {
        return recentlyViewedProductDao.observeProductIds()
    }

    suspend fun clearHistory() {
        recentlyViewedProductDao.clear()
    }

    suspend fun recordView(productId: Long) {
        recentlyViewedProductDao.record(
            productId = productId,
            nowMillis = clock(),
            limit = MAX_HISTORY_SIZE,
        )
    }
}
