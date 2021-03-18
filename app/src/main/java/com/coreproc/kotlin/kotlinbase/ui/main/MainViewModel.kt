package com.coreproc.kotlin.kotlinbase.ui.main

import android.database.Observable
import androidx.lifecycle.MutableLiveData
import com.coreproc.kotlin.kotlinbase.data.remote.usecase.ApiUseCase
import com.coreproc.kotlin.kotlinbase.extensions.ObservableExtensions
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel
@Inject
constructor(private val apiUseCase: ApiUseCase) : BaseViewModel() {

    val success = MutableLiveData<String>()

    fun getSomething() {
        compositeDisposable.add(
            ObservableExtensions.defaultSubscribeBy(
                defaultObservable(apiUseCase.run("")), this) {
                success.postValue(it)
            }
        )
    }

}