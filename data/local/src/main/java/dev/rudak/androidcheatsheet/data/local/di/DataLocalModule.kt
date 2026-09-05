package dev.rudak.androidcheatsheet.data.local.di

import androidx.room.Room
import dev.rudak.androidcheatsheet.data.local.database.AppDatabase
import dev.rudak.androidcheatsheet.data.local.datasource.FavoriteProductLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataLocalModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "android-cheatsheet.db",
        ).build()
    }

    single {
        get<AppDatabase>().favoriteProductDao()
    }

    single {
        FavoriteProductLocalDataSource(
            favoriteProductDao = get(),
        )
    }
}