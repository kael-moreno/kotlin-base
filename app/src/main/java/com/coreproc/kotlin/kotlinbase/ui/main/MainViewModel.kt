package com.coreproc.kotlin.kotlinbase.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.coreproc.kotlin.kotlinbase.data.remote.RemoteApiStatusEnum
import com.coreproc.kotlin.kotlinbase.data.remote.Resource
import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import com.coreproc.kotlin.kotlinbase.data.remote.usecase.ApiUseCase
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val apiUseCase: ApiUseCase) : BaseViewModel() {

    val success = MutableLiveData<SampleResponse>()

    fun getSomething(owner: LifecycleOwner) {
        Timber.e("Called")
        loading.postValue(true)
        liveData(Dispatchers.IO) {
            // yield() = to cancel
            try {
                emit(apiUseCase.run(""))
            } catch (exception: Exception) {
                loading.postValue(false)
                Timber.e("ERROR $exception")
                emit(Resource(RemoteApiStatusEnum.ERROR, message = exception.message))
            }
        }.observe(owner) {
            loading.postValue(false)
            when (it.status) {
                RemoteApiStatusEnum.SUCCESS -> {
                    success.postValue(it.data!!)
                }
                RemoteApiStatusEnum.ERROR -> {

                }
            }
        }
    }



}