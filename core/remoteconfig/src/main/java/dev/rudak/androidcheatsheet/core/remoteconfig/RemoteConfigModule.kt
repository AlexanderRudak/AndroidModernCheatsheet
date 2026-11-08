package dev.rudak.androidcheatsheet.core.remoteconfig

import android.system.Os.bind
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteConfigModule
    get() = module {

        single {
            RemoteConfigFeatures(
                features = listOf(UseMviFeature)
            )
        }

        single {
            FirebaseRemoteConfig.getInstance()
        }

        single<RemoteConfigRepository> {
            RemoteConfigRepositoryImpl.create(
                remoteConfig = get(),
                isDebug = BuildConfig.DEBUG
            )
        }

        //использует конструктор реализации из соответствующего source set.
        // В Debug Koin разрешит три зависимости, в Release — две.
        //Нужно перенести только регистрацию RemoteConfigInteractorImpl из общего DI-модуля в соответствующие source sets.
        //singleOf(::RemoteConfigInteractorImpl) {
        //    bind<RemoteConfigInteractor>()
        //}
    }