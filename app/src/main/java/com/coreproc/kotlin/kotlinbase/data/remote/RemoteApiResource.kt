package com.coreproc.kotlin.kotlinbase.data.remote

sealed class ResponseHandler<out T>(
    val result: T? = null,
    val loading: Boolean = false,
    val errorBody: ErrorBody? = null,
    val exception: Throwable? = null
) {
    class Loading<T>(loading: Boolean = false) : ResponseHandler<T>(loading = loading)
    class Success<T>(data: T) : ResponseHandler<T>(result = data, loading = false)
    class Error<T>(errorBody: ErrorBody) : ResponseHandler<T>(errorBody = errorBody, loading = false)
    class Failure<T>(exception: Throwable) : ResponseHandler<T>(exception = exception, loading = false)
}