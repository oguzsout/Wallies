package com.oguzdogdu.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "THEME_KEYS"
)

class DataStoreImpl @Inject constructor(
    private val context: Context,
) : DataStore {
    override suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getString(key: String): Flow<Resource<String?>> {
        return flow {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
            emit(preference[preferencesKey])
        }.toResource()
    }

    override suspend fun clearPReferences(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            if (it.contains(preferencesKey)) {
                it.remove(preferencesKey)
            }
        }
    }
}