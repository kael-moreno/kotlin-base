package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.extensions.postError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseViewModel: ViewModel() {

    val loading = Channel<Boolean>()
    val error = Channel<ErrorBody>()
    val failure = Channel<Throwable>()
    val unauthorized = Channel<Boolean>()
    val noInternetConnection = Channel<Throwable>()

    /**
     * Binds the ViewModel's channels to the BaseActivity's UI handling methods.
     * This sets up observers for loading, error, failure, unauthorized access, and no internet connection events.
     *
     * @param baseActivity The BaseActivity instance to bind to
     */
    fun bindActivity(baseActivity: BaseActivity) {
        loading.receiveAsFlow().onEach {
            baseActivity.loading(it)
        }.launchIn(baseActivity.lifecycleScope)

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