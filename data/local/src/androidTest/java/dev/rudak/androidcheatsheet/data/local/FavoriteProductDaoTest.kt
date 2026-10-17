package dev.rudak.androidcheatsheet.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.rudak.androidcheatsheet.data.local.dao.FavoriteProductDao
import dev.rudak.androidcheatsheet.data.local.database.AppDatabase
import dev.rudak.androidcheatsheet.data.local.entity.FavoriteProductEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteProductDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: FavoriteProductDao

    @Before
    fun setup() {
        // Создаём базу Room в памяти: каждый тест работает с чистыми данными.
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
        // Закрываем базу после каждого теста.
        database.close()
    }

    @Test
    fun insertFavoriteProduct_returnsFavoriteId() = runTest {
        // После добавления товара DAO должен вернуть его идентификатор.
        dao.insert(FavoriteProductEntity(productId = 1L))

        val favoriteIds = dao.getFavoriteIds()

        assertEquals(listOf(1L), favoriteIds)
    }

    @Test
    fun deleteFavoriteProduct_removesFavoriteId() = runTest {
        // После удаления товара список избранного должен стать пустым.
        dao.insert(FavoriteProductEntity(productId = 1L))
        dao.deleteByProductId(1L)

        val favoriteIds = dao.getFavoriteIds()

        assertEquals(emptyList<Long>(), favoriteIds)
    }

    @Test
    fun observeFavoriteIds_reflectsInsertAndDelete() = runTest {
        // Проверяем, что Flow отражает актуальное состояние базы:
        // сначала пусто, после добавления есть ID, после удаления снова пусто.
        assertEquals(emptyList<Long>(), dao.observeFavoriteIds().first())

        dao.insert(FavoriteProductEntity(productId = 1L))
        assertEquals(listOf(1L), dao.observeFavoriteIds().first())

        dao.deleteByProductId(1L)
        assertEquals(emptyList<Long>(), dao.observeFavoriteIds().first())
    }
}
