package dev.rudak.androidcheatsheet.core.remoteconfig

import kotlinx.coroutines.flow.Flow

interface DebugConfigRepository {

    suspend fun getOverride(key: String): String?

    suspend fun setOverride(key: String, value: String)

    suspend fun clearOverride(key: String)

    fun observeOverrides(): Flow<Map<String, String>>
}