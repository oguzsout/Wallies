package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface DataStore {
    suspend fun putString(key:String,value:String)
    suspend fun putBoolean(key:String,value:Boolean)
    suspend fun getString(key: String):Flow<Resource<String?>>
    suspend fun clearPReferences(key: String)
}