package dev.rudak.androidcheatsheet.data.local.di

import androidx.room.Room
import dev.rudak.androidcheatsheet.data.local.database.AppDatabase
import dev.rudak.androidcheatsheet.data.local.database.MIGRATION_1_2
import dev.rudak.androidcheatsheet.data.local.datasource.FavoriteProductLocalDataSource
import dev.rudak.androidcheatsheet.data.local.datasource.RecentlyViewedLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataLocalModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "android-cheatsheet.db",
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single {
        get<AppDatabase>().favoriteProductDao()
    }

    single {
        get<AppDatabase>().recentlyViewedProductDao()
    }

    single {
        RecentlyViewedLocalDataSource(
            recentlyViewedProductDao = get(),
        )
    }

    single {
        FavoriteProductLocalDataSource(
            favoriteProductDao = get(),
        )
    }
}