package dev.rudak.androidcheatsheet

import org.koin.core.module.Module
import dev.rudak.androidcheatsheet.core.remoteconfig.releaseConfigModule
import dev.rudak.androidcheatsheet.navigation.RemoteConfigDestination
import dev.rudak.androidcheatsheet.navigation.RemoteConfigDestinationImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


fun appKoinModules(): List<Module> =
    commonKoinModules() +
            releaseConfigModule +
            module {
                singleOf(::RemoteConfigDestinationImpl) {
                    bind<RemoteConfigDestination>()
                }
            }