package dev.rudak.androidcheatsheet.domain.usecase.di

import dev.rudak.androidcheatsheet.domain.usecase.preferences.ObserveShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.preferences.SetShowOnlyFavoritesUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetPagedProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductByIdUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductDetailsInteractor
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetProductsWithErrorUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetRecommendedProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ToggleFavoriteUseCase
import org.koin.dsl.module

val domainUseCaseModule = module {
    factory {
        ObserveShowOnlyFavoritesUseCase(
            repository = get(),
        )
    }

    factory {
        SetShowOnlyFavoritesUseCase(
            repository = get(),
        )
    }

    factory {
        GetProductsUseCase(
            productRepository = get(),
        )
    }
    factory {
        GetProductByIdUseCase(
            repository = get(),
        )
    }
    factory {
        GetProductsWithErrorUseCase(
            productRepository = get(),
        )
    }

    single {
        ToggleFavoriteUseCase(
            productRepository = get(),
        )
    }

    factory {
        GetPagedProductsUseCase(
            productPagingRepository = get(),
        )
    }

//    single {
//        ObserveProductByIdUseCase(
//            productRepository = get(),
//        )
//    }

    single {
        GetRecommendedProductsUseCase(
            productRepository = get(),
        )
    }

    single {
        GetProductDetailsInteractor(
            getProductByIdUseCase = get(),
            getRecommendedProductsUseCase = get(),
        )
    }

//    single {
//        ObserveProductsUseCase(
//            productRepository = get(),
//        )
//    }
}