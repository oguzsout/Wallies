package com.oguzdogdu.domain.wrapper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.toResource(): Flow<Resource<T>> {
    return map<T, Resource<T>> {
        Resource.Success(it)
    }
        .onStart {
            emit(Resource.Loading)
        }
        .catch { exception ->
            emit(Resource.Error(exception.message ?: exception.toString()))
        }
}