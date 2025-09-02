package com.coreproc.kotlin.kotlinbase.ui.main

import androidx.lifecycle.viewModelScope
import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import com.coreproc.kotlin.kotlinbase.data.remote.usecase.ApiUseCase
import com.coreproc.kotlin.kotlinbase.extensions.handleResponse
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val apiUseCase: ApiUseCase) : BaseViewModel() {

    private val successChannel = Channel<SampleResponse>()
    val successFlow = successChannel.receiveAsFlow()

    fun getSomething() = viewModelScope.launch(Dispatchers.IO) {
        apiUseCase.run("")
            .collectLatest { resource ->

                /**
                 * If needed to manually handle the response, do it here.
                 * Make sure to remove the handleResponse extension function below.
                 * Example:
                 * when (resource) {
                 *    is ResponseHandler.Loading -> {
                 *    loading.send(resource.loading)
                 *    }
                 *    is ResponseHandler.Error -> {
                 *    error.send(resource.errorBody!!)
                 *    }
                 *    is ResponseHandler.Failure -> {
                 *    failure.send(resource.exception ?: Throwable(message = "Unknown error occurred"))
                 *    }
                 *    is ResponseHandler.Success -> {
                 *    loading.send(false)
                 *    resource.result?.let {
                 *    successChannel.send(it)
                 *    }
                 *    }
                 *    else -> {
                 *    // Nothing to do here
                 *    }
                 *    }
                 *    OR
                 *    just use the handleResponse extension function as shown below.
                 */

                resource.handleResponse(this@MainViewModel) {
                    successChannel.send(it)
                }
            }
    }
}