package dev.rudak.androidcheatsheet.core.analytics.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEvent
import dev.rudak.androidcheatsheet.core.analytics.AnalyticSenderController

class FirebaseAnalyticSenderController(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticSenderController {

    override fun sendEvent(event: AnalyticEvent) {
        firebaseAnalytics.logEvent(
            event.name,
            event.params.toBundle(),
        )
    }

    private fun Map<String, Any>.toBundle(): Bundle {
        return Bundle().apply {
            forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is Float -> putFloat(key, value)
                    is Boolean -> putBoolean(key, value)
                }
            }
        }
    }
}