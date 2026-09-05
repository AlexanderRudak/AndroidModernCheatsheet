package dev.rudak.androidcheatsheet.domain.usecase.preferences

import dev.rudak.androidcheatsheet.domain.repository.preferences.ShopPreferencesRepository
import kotlinx.coroutines.flow.Flow

class ObserveShowOnlyFavoritesUseCase(
    private val repository: ShopPreferencesRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.showOnlyFavorites
    }
}