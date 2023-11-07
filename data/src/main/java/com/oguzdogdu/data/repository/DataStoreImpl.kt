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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

private val Context.themeDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "THEME_KEYS"
)

private val Context.languageDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "language_preference"
)
private val Context.loginDialog: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "login_dialog_present"
)


class DataStoreImpl @Inject constructor(
    private val context: Context,
) : DataStore {
    override suspend fun putThemeStrings(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.themeDataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getThemeStrings(key: String): Flow<Resource<String?>> {
        return flow {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.themeDataStore.data.first()
            emit(preference[preferencesKey])
        }.toResource()
    }

    override suspend fun putLanguageStrings(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.languageDataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getLanguageStrings(key: String): Flow<Resource<String?>> {
        return flow {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.languageDataStore.data.first()
            emit(preference[preferencesKey])
        }.toResource()
    }

    override suspend fun clearPReferences(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.themeDataStore.edit {
            if (it.contains(preferencesKey)) {
                it.remove(preferencesKey)
            }
        }
    }

    override suspend fun setShowLoginWarningPresent(key: String,value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.loginDialog.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getLoginWarningPresent(key: String): Flow<Boolean> {
        return flow {
            val preferencesKey = booleanPreferencesKey(key)
            val preference = context.loginDialog.data.firstOrNull()
            emit(preference?.get(preferencesKey)!!)
        }
    }
}