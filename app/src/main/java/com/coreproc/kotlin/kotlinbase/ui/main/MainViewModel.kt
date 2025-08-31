package com.coreproc.kotlin.kotlinbase.ui.main

import androidx.lifecycle.viewModelScope
import com.coreproc.kotlin.kotlinbase.data.remote.DefaultState
import com.coreproc.kotlin.kotlinbase.data.remote.ResponseHandler
import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import com.coreproc.kotlin.kotlinbase.data.remote.usecase.ApiUseCase
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val apiUseCase: ApiUseCase) : BaseViewModel() {

    private val successMutableStateFlow = MutableStateFlow<DefaultState<SampleResponse>>(DefaultState())
    val successStateFlow = successMutableStateFlow.asStateFlow()

    fun getSomething() = viewModelScope.launch(Dispatchers.IO) {
        apiUseCase.run("")
            .collectLatest { resource ->
                when (resource) {
                    is ResponseHandler.Loading -> successMutableStateFlow.update {
                        DefaultState(isLoading = resource.loading)
                    }
                    is ResponseHandler.Success -> successMutableStateFlow.update {
                        DefaultState(data = resource.result)
                    }
                    is ResponseHandler.Error -> successMutableStateFlow.update {
                        DefaultState(error = resource.errorBody)
                    }
                    is ResponseHandler.Failure -> successMutableStateFlow.update {
                        DefaultState(failureMessage = resource.exception?.message)
                    }
                }
            }
    }
}