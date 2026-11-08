package dev.rudak.androidcheatsheet.core.remoteconfig

interface RemoteConfigInteractor {

    suspend fun fetch(): Boolean

    suspend fun getString(feature: RemoteConfigFeature): String

    suspend fun getBoolean(feature: RemoteConfigFeature): Boolean

    suspend fun getInt(feature: RemoteConfigFeature): Int

    suspend fun getLong(feature: RemoteConfigFeature): Long

    suspend fun getDouble(feature: RemoteConfigFeature): Double
}