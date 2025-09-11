package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.ViewModel
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseViewModel: ViewModel() {

    // Use StateFlow for loading to retain state for Compose
    private val _loading = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loading.asStateFlow()

    // Convert channels to StateFlows for better Compose integration
    private val _error = MutableStateFlow<ErrorBody?>(null)
    val errorStateFlow: StateFlow<ErrorBody?> = _error.asStateFlow()

    private val _failure = MutableStateFlow<Throwable?>(null)
    val failureStateFlow: StateFlow<Throwable?> = _failure.asStateFlow()

    private val _unauthorized = MutableStateFlow(false)
    val unauthorizedStateFlow: StateFlow<Boolean> = _unauthorized.asStateFlow()

    private val _noInternetConnection = MutableStateFlow<Throwable?>(null)
    val noInternetConnectionStateFlow: StateFlow<Throwable?> = _noInternetConnection.asStateFlow()

    /**
     * Helper function to set loading state that updates both Channel and StateFlow
     */
    suspend fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    /**
     * Helper function to set error state that updates both Channel and StateFlow
     */
    suspend fun setError(errorBody: ErrorBody) {
        _loading.value = false
        _error.value = errorBody
    }

    /**
     * Helper function to set failure state that updates both Channel and StateFlow
     */
    suspend fun setFailure(throwable: Throwable) {
        _loading.value = false
        _failure.value = throwable
    }

    /**
     * Helper function to set unauthorized state that updates both Channel and StateFlow
     */
    suspend fun setUnauthorized(isUnauthorized: Boolean) {
        _loading.value = false
        _unauthorized.value = isUnauthorized
    }

    /**
     * Helper function to set no internet connection state that updates both Channel and StateFlow
     */
    suspend fun setNoInternetConnection(throwable: Throwable) {
        _loading.value = false
        _noInternetConnection.value = throwable
    }

    /**
     * Helper function to clear error state
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Helper function to clear failure state
     */
    fun clearFailure() {
        _failure.value = null
    }

    /**
     * Helper function to clear unauthorized state
     */
    fun clearUnauthorized() {
        _unauthorized.value = false
    }

    /**
     * Helper function to clear no internet connection state
     */
    fun clearNoInternetConnection() {
        _noInternetConnection.value = null
    }
}