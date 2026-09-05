package dev.rudak.androidcheatsheet.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.rudak.androidcheatsheet.data.local.dao.FavoriteProductDao
import dev.rudak.androidcheatsheet.data.local.database.AppDatabase
import dev.rudak.androidcheatsheet.data.local.entity.FavoriteProductEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteProductDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: FavoriteProductDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        )
            .allowMainThreadQueries()
            .build()

        dao = database.favoriteProductDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertFavoriteProduct_returnsFavoriteId() = runTest {
        dao.insert(FavoriteProductEntity(productId = 1L))

        val favoriteIds = dao.getFavoriteIds()

        assertEquals(listOf(1L), favoriteIds)
    }

    @Test
    fun deleteFavoriteProduct_removesFavoriteId() = runTest {
        dao.insert(FavoriteProductEntity(productId = 1L))

        dao.deleteByProductId(1L)

        val favoriteIds = dao.getFavoriteIds()

        assertEquals(emptyList<Long>(), favoriteIds)
    }
}