package dev.rudak.androidcheatsheet.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.rudak.androidcheatsheet.data.local.dao.FavoriteProductDao
import dev.rudak.androidcheatsheet.data.local.dao.RecentlyViewedProductDao
import dev.rudak.androidcheatsheet.data.local.entity.FavoriteProductEntity
import dev.rudak.androidcheatsheet.data.local.entity.RecentlyViewedProductEntity

@Database(
    entities = [
        FavoriteProductEntity::class,
        RecentlyViewedProductEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteProductDao(): FavoriteProductDao

    abstract fun recentlyViewedProductDao(): RecentlyViewedProductDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `recently_viewed_products` (" +
                "`productId` INTEGER NOT NULL, " +
                "`viewedAt` INTEGER NOT NULL, " +
                "PRIMARY KEY(`productId`))"
        )
    }
}