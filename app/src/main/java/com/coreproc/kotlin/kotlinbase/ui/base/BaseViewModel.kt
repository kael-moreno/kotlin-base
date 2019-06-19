package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<ErrorBody>()
    val unauthorized = SingleLiveEvent<Boolean>()
    val noInternetConnection = SingleLiveEvent<Throwable>()

    fun observeCommonEvent(baseActivity: BaseActivity) {
        loading.observe(baseActivity, Observer { baseActivity.loading(it) })
        error.observe(baseActivity, Observer { baseActivity.error(it) })
        unauthorized.observe(baseActivity, Observer { baseActivity.unathorized(it) })
        noInternetConnection.observe(baseActivity, Observer { baseActivity.noInternetConnection(it) })
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

    open fun removeObservers(owner: () -> Lifecycle) {
        loading.removeObservers(owner)
        error.removeObservers(owner)
        unauthorized.removeObservers(owner)
        noInternetConnection.removeObservers(owner)
    }

}