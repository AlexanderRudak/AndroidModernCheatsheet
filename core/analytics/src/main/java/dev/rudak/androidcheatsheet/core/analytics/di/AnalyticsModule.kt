package dev.rudak.androidcheatsheet.core.analytics.di


import com.google.firebase.analytics.FirebaseAnalytics
import dev.rudak.androidcheatsheet.core.analytics.AnalyticSenderController
import dev.rudak.androidcheatsheet.core.analytics.firebase.FirebaseAnalyticSenderController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val analyticsModule = module {

    single<FirebaseAnalytics> {
        FirebaseAnalytics.getInstance(androidContext())
    }

    single<AnalyticSenderController> {
        FirebaseAnalyticSenderController(
            firebaseAnalytics = get(),
        )
    }
}