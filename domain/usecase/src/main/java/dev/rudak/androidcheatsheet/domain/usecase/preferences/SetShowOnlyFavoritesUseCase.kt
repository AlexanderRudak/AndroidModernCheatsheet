package dev.rudak.androidcheatsheet.domain.usecase.preferences

import dev.rudak.androidcheatsheet.domain.repository.preferences.ShopPreferencesRepository

class SetShowOnlyFavoritesUseCase(
    private val repository: ShopPreferencesRepository,
) {
    suspend operator fun invoke(value: Boolean) {
        repository.setShowOnlyFavorites(value)
    }
}