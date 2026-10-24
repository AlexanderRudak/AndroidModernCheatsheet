package dev.rudak.androidcheatsheet.feature.mvvm.di

import dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel.MvvmViewModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel.ProductDetailsViewModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel.RecentlyViewedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureMvvmModule = module {
    viewModel {
        MvvmViewModel(
            //observeProductsUseCase = get(),
            getProductsUseCase = get(),
            getProductsWithErrorUseCase = get(),
            observeShowOnlyFavoritesUseCase = get(),
            setShowOnlyFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            analyticController = get(),
        )
    }
    viewModel { params ->
        ProductDetailsViewModel(
            productId = params.get(),
            //getProductByIdUseCase = get(),
            getProductDetailsInteractor = get(),
            recordProductViewUseCase = get(),
        )
    }
    viewModel {
        RecentlyViewedViewModel(
            observeRecentlyViewedProductsUseCase = get(),
            clearRecentlyViewedProductsUseCase = get(),
        )
    }
}