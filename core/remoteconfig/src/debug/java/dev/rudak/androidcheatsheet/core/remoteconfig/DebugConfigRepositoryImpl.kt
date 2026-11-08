package dev.rudak.androidcheatsheet.core.remoteconfig

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.debugConfigDataStore by preferencesDataStore(
    name = "debug_remote_config"
)

class DebugConfigRepositoryImpl(
    context: Context
) : DebugConfigRepository {

    private val dataStore =
        context.applicationContext.debugConfigDataStore

    override suspend fun getOverride(key: String): String? =
        dataStore.data.first()[stringPreferencesKey(key)]

    override suspend fun setOverride(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun clearOverride(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    override fun observeOverrides(): Flow<Map<String, String>> =
        dataStore.data.map { preferences ->
            preferences.asMap().mapNotNull { (key, value) ->
                (value as? String)?.let { key.name to it }
            }.toMap()
        }
}