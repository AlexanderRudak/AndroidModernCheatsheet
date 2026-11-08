package dev.rudak.androidcheatsheet.core.remoteconfig

class RemoteConfigInteractorImpl(
    private val repository: RemoteConfigRepository
) : RemoteConfigInteractor {

    override suspend fun fetch(): Boolean =
        repository.fetch()

    override suspend fun getString(
        feature: RemoteConfigFeature
    ): String =
        repository.getString(feature.key)

    override suspend fun getBoolean(
        feature: RemoteConfigFeature
    ): Boolean =
        repository.getBoolean(feature.key)

    override suspend fun getInt(
        feature: RemoteConfigFeature
    ): Int =
        repository.getInt(feature.key)

    override suspend fun getLong(
        feature: RemoteConfigFeature
    ): Long =
        repository.getLong(feature.key)

    override suspend fun getDouble(
        feature: RemoteConfigFeature
    ): Double =
        repository.getDouble(feature.key)
}
