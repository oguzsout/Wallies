package com.oguzdogdu.data.common

import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T,
): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    emit(Resource.Success(apiCall.invoke()))
}.catch { throwable ->
    when (throwable) {
        is IOException -> emit(Resource.Error(throwable.message.orEmpty()))
        is HttpException -> {
            val errorBody = throwable.response()?.errorBody().toString()
            emit(Resource.Error(errorBody))
        }

        is SocketTimeoutException -> {
            val errorBody = throwable.message.orEmpty()
            emit(Resource.Error(errorBody))
        }

        else -> emit(Resource.Error("An unexpected error occurred"))
    }
}.flowOn(dispatcher)