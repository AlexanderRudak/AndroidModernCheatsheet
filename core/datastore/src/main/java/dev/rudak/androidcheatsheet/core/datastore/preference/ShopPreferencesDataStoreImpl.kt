package dev.rudak.androidcheatsheet.core.datastore.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopPreferencesDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
) : ShopPreferencesDataStore {

    override val showOnlyFavorites: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[SHOW_ONLY_FAVORITES_KEY] ?: false
        }

    override suspend fun setShowOnlyFavorites(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_ONLY_FAVORITES_KEY] = value
        }
    }

    private companion object {
        val SHOW_ONLY_FAVORITES_KEY = booleanPreferencesKey("show_only_favorites")
    }
}