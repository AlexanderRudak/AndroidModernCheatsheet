package dev.rudak.androidcheatsheet.core.remoteconfig

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val debugConfigModule
    get() = module {
        singleOf(::DebugConfigRepositoryImpl) {
            bind<DebugConfigRepository>()
        }

        singleOf(::RemoteConfigInteractorImpl) {
            bind<RemoteConfigInteractor>()
        }
    }