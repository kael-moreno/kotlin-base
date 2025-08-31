package com.coreproc.kotlin.kotlinbase.data.remote

sealed class ResponseHandler<out T>(
    val result: T? = null,
    val loading: Boolean = false,
    val errorBody: ErrorBody? = null,
    val exception: Throwable? = null
) {
    class Loading<T>(loading: Boolean) : ResponseHandler<T>()
    class Success<T>(data: T) : ResponseHandler<T>(result = data)
    class Error<T>(errorBody: ErrorBody) : ResponseHandler<T>(errorBody = errorBody)
    class Failure<T>(exception: Throwable) : ResponseHandler<T>(exception = exception)
}