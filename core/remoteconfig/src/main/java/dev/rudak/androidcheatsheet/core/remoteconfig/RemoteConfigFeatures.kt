package dev.rudak.androidcheatsheet.core.remoteconfig

/*
реестр всех параметров Remote Config
 */
class RemoteConfigFeatures(
    val features: List<RemoteConfigFeature>
) {
    fun findByKey(key: String): RemoteConfigFeature? =
        features.find { it.key == key }
}