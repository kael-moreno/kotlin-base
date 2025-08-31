package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber


abstract class BaseViewModel: ViewModel() {

    val loading = Channel<Boolean>()
    val error = Channel<ErrorBody>()
    val failure = Channel<String>()
    val unauthorized = Channel<Boolean>()
    val noInternetConnection = Channel<Throwable>()

    fun bindActivity(baseActivity: BaseActivity) {
        loading.receiveAsFlow().onEach {
            Timber.e("Loading $it")
            baseActivity.loading(it)
        }.launchIn(baseActivity.lifecycleScope)

        error.receiveAsFlow().onEach {
            baseActivity.error(it)
        }.launchIn(baseActivity.lifecycleScope)

        unauthorized.receiveAsFlow().onEach {
            baseActivity.unauthorized(it)
        }.launchIn(baseActivity.lifecycleScope)

        noInternetConnection.receiveAsFlow().onEach {
            baseActivity.noInternetConnection(it)
        }.launchIn(baseActivity.lifecycleScope)
    }

}