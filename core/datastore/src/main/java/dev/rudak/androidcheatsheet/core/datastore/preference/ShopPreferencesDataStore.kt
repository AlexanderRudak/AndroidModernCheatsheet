package dev.rudak.androidcheatsheet.core.datastore.preference

import kotlinx.coroutines.flow.Flow

interface ShopPreferencesDataStore {

    val showOnlyFavorites: Flow<Boolean>

    suspend fun setShowOnlyFavorites(value: Boolean)
}