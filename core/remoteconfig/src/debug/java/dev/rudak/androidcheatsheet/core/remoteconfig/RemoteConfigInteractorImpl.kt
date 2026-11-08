package dev.rudak.androidcheatsheet.core.remoteconfig

class RemoteConfigInteractorImpl(
    private val repository: RemoteConfigRepository,
    private val debugConfigRepository: DebugConfigRepository,
    private val remoteConfigFeatures: RemoteConfigFeatures
) : RemoteConfigInteractor {

    override suspend fun fetch(): Boolean =
        repository.fetch()

    override suspend fun getString(
        feature: RemoteConfigFeature
    ): String {
        checkFeature(feature)

        val override = debugConfigRepository
            .getOverride(feature.key)
            ?.takeIf { it.isNotEmpty() }

        return override
            ?: repository.getString(feature.key)
                .ifEmpty { feature.debugValue }
    }

    override suspend fun getBoolean(
        feature: RemoteConfigFeature
    ): Boolean =
        getString(feature).toBoolean()

    override suspend fun getInt(
        feature: RemoteConfigFeature
    ): Int =
        getString(feature).toInt()

    override suspend fun getLong(
        feature: RemoteConfigFeature
    ): Long =
        getString(feature).toLong()

    override suspend fun getDouble(
        feature: RemoteConfigFeature
    ): Double =
        getString(feature).toDouble()

    private fun checkFeature(feature: RemoteConfigFeature) {
        check(remoteConfigFeatures.findByKey(feature.key) != null) {
            "Feature ${feature.key} is not registered in RemoteConfigFeatures"
        }
    }
}
