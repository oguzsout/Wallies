package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface DataStore {
    suspend fun putThemeStrings(key:String, value:String)
    suspend fun getThemeStrings(key: String):Flow<Resource<String?>>
    suspend fun putLanguageStrings(key:String, value:String)
    suspend fun getLanguageStrings(key: String):Flow<Resource<String?>>
    suspend fun clearPReferences(key: String)
}