package dev.rudak.androidcheatsheet.data.repository.di

import dev.rudak.androidcheatsheet.data.repository.api.ProductApi
import dev.rudak.androidcheatsheet.data.repository.api.FakeProductApi
import dev.rudak.androidcheatsheet.data.repository.api.RetrofitProductApi
import dev.rudak.androidcheatsheet.data.repository.api.RetrofitProductService
import dev.rudak.androidcheatsheet.data.repository.paging.ProductPagingRepositoryImpl
import dev.rudak.androidcheatsheet.data.repository.preferences.ShopPreferencesRepositoryImpl
import dev.rudak.androidcheatsheet.data.repository.shop.ProductRepositoryImpl
import dev.rudak.androidcheatsheet.domain.repository.preferences.ShopPreferencesRepository
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import dev.rudak.androidcheatsheet.data.repository.remote.ProductJsonLoader
import dev.rudak.androidcheatsheet.domain.repository.paging.ProductPagingRepository
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val USE_FAKE_API = true

val dataRepositoryModule = module {
    single<RetrofitProductService> {
        get<retrofit2.Retrofit>().create(RetrofitProductService::class.java)
    }

    single<ShopPreferencesRepository> {
        ShopPreferencesRepositoryImpl(
            dataStore = get(),
        )
    }

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single<ProductPagingRepository> {
        ProductPagingRepositoryImpl(
            productRepository = get(),
        )
    }

    single {
        ProductJsonLoader(
            context = get(),
            json = get(),
        )
    }

//    single<ProductApi> {
//        FakeProductApi()
//    }
    single<ProductApi> {
        if (USE_FAKE_API) {
            FakeProductApi(productJsonLoader = get(),)
        } else {
            RetrofitProductApi(
                service = get(),
            )
        }
    }

    single<ProductRepository> {
        ProductRepositoryImpl(
            productApi = get(),
            favoriteProductLocalDataSource = get(),
        )
    }
}