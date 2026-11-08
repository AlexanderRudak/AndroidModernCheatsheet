package dev.rudak.androidcheatsheet.core.remoteconfig

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class RemoteConfigRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    override suspend fun fetch(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch Remote Config", e)
            false
        }
    }

    override fun getString(key: String): String =
        remoteConfig.getString(key)

    override fun getBoolean(key: String): Boolean =
        remoteConfig.getBoolean(key)

    override fun getInt(key: String): Int =
        remoteConfig.getLong(key).toInt()

    override fun getLong(key: String): Long =
        remoteConfig.getLong(key)

    override fun getDouble(key: String): Double =
        remoteConfig.getDouble(key)

    companion object {
        private const val TAG = "RemoteConfig"

        fun create(
            remoteConfig: FirebaseRemoteConfig,
            isDebug: Boolean
        ): RemoteConfigRepositoryImpl {

            val settings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(
                    if (isDebug) 10L else 43_200L
                )
                .build()

            remoteConfig.setConfigSettingsAsync(settings)

            return RemoteConfigRepositoryImpl(remoteConfig)
        }
    }
}

