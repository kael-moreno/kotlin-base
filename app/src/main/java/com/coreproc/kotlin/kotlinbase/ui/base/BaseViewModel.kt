package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel: ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<ErrorBody>()
    private val unauthorized = SingleLiveEvent<Boolean>()
    val noInternetConnection = SingleLiveEvent<Throwable>()

    fun observeCommonEvent(baseActivity: BaseActivity) {
        loading.observe(baseActivity) { baseActivity.loading(it) }
        error.observe(baseActivity) { baseActivity.error(it) }
        unauthorized.observe(baseActivity) { baseActivity.unauthorized(it) }
        noInternetConnection.observe(baseActivity) { baseActivity.noInternetConnection(it) }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    open fun removeObservers(owner: LifecycleOwner) {
        loading.removeObservers(owner)
        error.removeObservers(owner)
        unauthorized.removeObservers(owner)
        noInternetConnection.removeObservers(owner)
    }

}