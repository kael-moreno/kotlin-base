package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.extensions.postError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseViewModel: ViewModel() {

    // Use StateFlow for loading to retain state for Compose
    private val _loading = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loading.asStateFlow()

    val error = Channel<ErrorBody>()
    val failure = Channel<Throwable>()
    val unauthorized = Channel<Boolean>()
    val noInternetConnection = Channel<Throwable>()

    /**
     * Helper function to set loading state that updates both Channel and StateFlow
     */
    suspend fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    /**
     * Binds the ViewModel's channels to the BaseActivity's UI handling methods.
     * This sets up observers for loading, error, failure, unauthorized access, and no internet connection events.
     *
     * @param baseActivity The BaseActivity instance to bind to
     */
    fun bindActivity(baseActivity: BaseActivity) {
        error.receiveAsFlow().onEach {
            it.postError(this, baseActivity)
        }.launchIn(baseActivity.lifecycleScope)

        failure.receiveAsFlow().onEach {
            it.postError(this, baseActivity)
        }.launchIn(baseActivity.lifecycleScope)

        unauthorized.receiveAsFlow().onEach {
            baseActivity.unauthorized()
        }.launchIn(baseActivity.lifecycleScope)

        noInternetConnection.receiveAsFlow().onEach {
            baseActivity.noInternetConnection(it)
        }.launchIn(baseActivity.lifecycleScope)
    }

}