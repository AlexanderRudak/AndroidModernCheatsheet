package dev.rudak.androidcheatsheet.domain.repository.preferences

import kotlinx.coroutines.flow.Flow

interface ShopPreferencesRepository {

    val showOnlyFavorites: Flow<Boolean>

    suspend fun setShowOnlyFavorites(value: Boolean)
}