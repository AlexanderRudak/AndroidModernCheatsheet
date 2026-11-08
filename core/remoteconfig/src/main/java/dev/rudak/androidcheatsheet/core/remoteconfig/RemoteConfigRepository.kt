package dev.rudak.androidcheatsheet.core.remoteconfig

interface RemoteConfigRepository {

    //загружает и активирует конфигурацию с сервера Firebase
    suspend fun fetch(): Boolean

    fun getString(key: String): String

    fun getBoolean(key: String): Boolean

    fun getInt(key: String): Int

    fun getLong(key: String): Long

    fun getDouble(key: String): Double
}