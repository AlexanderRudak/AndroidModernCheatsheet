package dev.rudak.androidcheatsheet

import android.app.Application
import dev.rudak.androidcheatsheet.core.datastore.di.coreDataStoreModule
import dev.rudak.androidcheatsheet.core.network.di.coreNetworkModule
import dev.rudak.androidcheatsheet.data.local.di.dataLocalModule
import dev.rudak.androidcheatsheet.data.repository.di.dataRepositoryModule
import dev.rudak.androidcheatsheet.domain.usecase.di.domainUseCaseModule
import dev.rudak.androidcheatsheet.feature.mvi.presentation.di.featureMviModule
import dev.rudak.androidcheatsheet.feature.mvvm.di.featureMvvmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                dataLocalModule,
                coreNetworkModule,
                coreDataStoreModule,
                dataRepositoryModule,
                domainUseCaseModule,
                featureMvvmModule,
                featureMviModule,
            )
        }
    }
}