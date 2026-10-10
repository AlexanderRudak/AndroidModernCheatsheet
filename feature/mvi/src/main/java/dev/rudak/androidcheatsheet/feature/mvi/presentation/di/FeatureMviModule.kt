package dev.rudak.androidcheatsheet.feature.mvi.presentation.di

import dev.rudak.androidcheatsheet.feature.mvi.presentation.actor.MviActionActor
import dev.rudak.androidcheatsheet.feature.mvi.core.MviActor
import dev.rudak.androidcheatsheet.feature.mvi.presentation.actor.MviAnalyticActor
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.reducer.MviReducer
import dev.rudak.androidcheatsheet.feature.mvi.presentation.viewmodel.MviViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val featureMviModule = module {

    factory {
        MviReducer()
    }

    factory {
        MviActionActor(
            getProductsUseCase = get(),
            getProductsWithErrorUseCase = get(),
            toggleFavoriteUseCase = get(),
            setShowOnlyFavoritesUseCase = get(),
        )
    } bind MviActor::class

    factory {
        MviAnalyticActor(
            analyticController = get(),
        )
    } bind MviActor::class

        /*factory<MviActor<MviCommand, MviEvent>> {
        MviActionActor(
            getProductsUseCase = get(),
            getProductsWithErrorUseCase = get(),
            toggleFavoriteUseCase = get(),
            setShowOnlyFavoritesUseCase = get(),
        )
    }

    factory<MviActor<MviCommand, MviEvent>> {
        MviAnalyticActor(
            analyticController = get(),
        )
    }*/

    viewModel {
        MviViewModel(
            reducer = get(),
            actors = getAll(),
            getPagedProductsUseCase = get(),
        )
    }

}