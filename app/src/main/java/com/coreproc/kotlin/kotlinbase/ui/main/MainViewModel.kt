package com.coreproc.kotlin.kotlinbase.ui.main

import androidx.lifecycle.MutableLiveData
import com.coreproc.kotlin.kotlinbase.data.remote.usecase.ApiUseCase
import com.coreproc.kotlin.kotlinbase.misc.common.threadManageIoToUi
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainViewModel
@Inject
constructor(private val apiUseCase: ApiUseCase) : BaseViewModel() {

    val success = MutableLiveData<String>()

    fun getSomething() {
        compositeDisposable.add(apiUseCase.run("")
            .threadManageIoToUi()
            .doOnSubscribe {
                // load something here on start API Call
            }
            .subscribeBy(
                onNext = {
                    success.postValue(it)
                },
                onError = {
                    error.postValue(it)
                }
            )
        )
    }

}