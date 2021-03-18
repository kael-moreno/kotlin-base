package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.data.remote.UseCase
import com.coreproc.kotlin.kotlinbase.extensions.ObservableExtensions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<ErrorBody>()
    val unauthorized = SingleLiveEvent<Boolean>()
    val noInternetConnection = SingleLiveEvent<Throwable>()

    fun observeCommonEvent(baseActivity: BaseActivity) {
        loading.observe(baseActivity, { baseActivity.loading(it) })
        error.observe(baseActivity, { baseActivity.error(it) })
        unauthorized.observe(baseActivity, { baseActivity.unauthorized(it) })
        noInternetConnection.observe(baseActivity, { baseActivity.noInternetConnection(it) })
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

    fun <T> defaultObservable(observable: Observable<T>): Observable<T>  {
        var defaultObs = ObservableExtensions.threadManageIoToUi(observable)
        defaultObs = ObservableExtensions.addDefaultDoOn(defaultObs, this)
        return defaultObs
    }

}