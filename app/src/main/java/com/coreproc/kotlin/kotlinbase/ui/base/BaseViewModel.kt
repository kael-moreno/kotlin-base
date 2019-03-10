package com.coreproc.kotlin.kotlinbase.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Throwable>()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    open fun removeObservers(owner: LifecycleOwner) {
        loading.removeObservers(owner)
        error.removeObservers(owner)
    }

    open fun removeObservers(owner: () -> Lifecycle) {
        loading.removeObservers(owner)
        error.removeObservers(owner)
    }

}