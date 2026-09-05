package dev.rudak.androidcheatsheet.data.repository.preferences

import dev.rudak.androidcheatsheet.core.datastore.preference.ShopPreferencesDataStore
import dev.rudak.androidcheatsheet.domain.repository.preferences.ShopPreferencesRepository
import kotlinx.coroutines.flow.Flow

class ShopPreferencesRepositoryImpl(
    private val dataStore: ShopPreferencesDataStore,
) : ShopPreferencesRepository {

    override val showOnlyFavorites: Flow<Boolean>
        get() = dataStore.showOnlyFavorites

    override suspend fun setShowOnlyFavorites(value: Boolean) {
        dataStore.setShowOnlyFavorites(value)
    }
}