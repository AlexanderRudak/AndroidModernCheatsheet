package dev.rudak.androidcheatsheet

import dev.rudak.androidcheatsheet.core.analytics.di.analyticsModule
import dev.rudak.androidcheatsheet.core.datastore.di.coreDataStoreModule
import dev.rudak.androidcheatsheet.core.network.di.coreNetworkModule
import dev.rudak.androidcheatsheet.core.remoteconfig.remoteConfigModule
import dev.rudak.androidcheatsheet.data.local.di.dataLocalModule
import dev.rudak.androidcheatsheet.data.repository.di.dataRepositoryModule
import dev.rudak.androidcheatsheet.domain.usecase.di.domainUseCaseModule
import dev.rudak.androidcheatsheet.feature.mvi.presentation.di.featureMviModule
import dev.rudak.androidcheatsheet.feature.mvvm.di.featureMvvmModule
import org.koin.core.module.Module

fun commonKoinModules(): List<Module> = listOf(
    dataLocalModule,
    coreNetworkModule,
    coreDataStoreModule,
    dataRepositoryModule,
    domainUseCaseModule,
    featureMvvmModule,
    featureMviModule,
    analyticsModule,
    remoteConfigModule
)