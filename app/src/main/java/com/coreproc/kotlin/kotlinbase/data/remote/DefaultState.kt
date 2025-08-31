package com.coreproc.kotlin.kotlinbase.data.remote

data class DefaultState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: ErrorBody? = null,
    val failureMessage: String? = null
)
