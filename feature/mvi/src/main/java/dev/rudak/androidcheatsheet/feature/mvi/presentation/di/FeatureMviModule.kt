package dev.rudak.androidcheatsheet.feature.mvi.presentation.di

import dev.rudak.androidcheatsheet.feature.mvi.presentation.actor.MviActor
import dev.rudak.androidcheatsheet.feature.mvi.presentation.reducer.MviReducer
import dev.rudak.androidcheatsheet.feature.mvi.presentation.viewmodel.MviViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureMviModule = module {

    factory {
        MviReducer()
    }

    factory {
        MviActor(
            getProductsUseCase = get(),
            getProductsWithErrorUseCase = get(),
            toggleFavoriteUseCase = get(),
            setShowOnlyFavoritesUseCase = get(),
        )
    }

    viewModel {
        MviViewModel(
            reducer = get(),
            actor = get(),
            getPagedProductsUseCase = get(),
        )
    }
}