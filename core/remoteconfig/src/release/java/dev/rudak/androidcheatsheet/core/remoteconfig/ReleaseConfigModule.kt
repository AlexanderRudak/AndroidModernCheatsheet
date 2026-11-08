package dev.rudak.androidcheatsheet.core.remoteconfig

import android.system.Os.bind
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val releaseConfigModule
    get() = module {
        singleOf(::RemoteConfigInteractorImpl) {
            bind<RemoteConfigInteractor>()
        }
    }