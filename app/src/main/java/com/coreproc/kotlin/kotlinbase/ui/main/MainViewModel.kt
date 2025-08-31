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
                resource.handleResponse(this@MainViewModel) {
                    successChannel.send(it)
                }
            }
    }
}