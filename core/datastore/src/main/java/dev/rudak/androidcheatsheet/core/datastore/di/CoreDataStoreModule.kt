package dev.rudak.androidcheatsheet.core.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dev.rudak.androidcheatsheet.core.datastore.preference.ShopPreferencesDataStore
import dev.rudak.androidcheatsheet.core.datastore.preference.ShopPreferencesDataStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

val coreDataStoreModule = module {

    single {
        PreferenceDataStoreFactory.create(
            produceFile = {
                File(
                    androidContext().filesDir,
                    "shop_preferences.preferences_pb",
                )
            },
        )
    }

    single<ShopPreferencesDataStore> {
        ShopPreferencesDataStoreImpl(
            dataStore = get(),
        )
    }
}