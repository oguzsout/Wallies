package com.oguzdogdu.domain.wrapper

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val errorMessage: String) : Resource<Nothing>

}

inline fun <T> Resource<T>.onLoading(loading: () -> Unit): Resource<T> {
    if (this is Resource.Loading) loading()
    return this
}

inline fun <T> Resource<T>.onFailure(block: (String) -> Unit): Resource<T> {
    if (this is Resource.Error) block(errorMessage)
    return this
}

inline fun <T> Resource<T>.onSuccess(block: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) block(data)
    return this
}