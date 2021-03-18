package com.coreproc.kotlin.kotlinbase.ui.main

import androidx.lifecycle.MutableLiveData
import com.coreproc.kotlin.kotlinbase.data.remote.usecase.ApiUseCase
import com.coreproc.kotlin.kotlinbase.extensions.*
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel
@Inject
constructor(private val apiUseCase: ApiUseCase) : BaseViewModel() {

    val success = MutableLiveData<String>()

    fun getSomething() {
        compositeDisposable.add(apiUseCase.run("")
            .threadManageIoToUi()
            .addDefaultDoOn(this)
            .defaultSubscribeBy(this) {
                success.postValue(it)
            }
        )
    }

}