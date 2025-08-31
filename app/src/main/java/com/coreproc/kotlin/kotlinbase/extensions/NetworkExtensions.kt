package com.coreproc.kotlin.kotlinbase.extensions

import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.data.remote.ResponseHandler
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.NotYetConnectedException

suspend fun Throwable.postError(baseViewModel: BaseViewModel, baseActivity: BaseActivity) {
    baseViewModel.loading.send(false)
    when (this) {
        is NotYetConnectedException, is UnknownHostException, is SocketTimeoutException, is ConnectionShutdownException, is IOException -> {
            baseViewModel.noInternetConnection.send(this)
        }
        is ErrorBody -> {
            if (this.http_code == 401) {
                baseViewModel.unauthorized.send(true)
            } else {
                baseActivity.error(this)
            }
        }
        else -> {
            val errorBody = ErrorBody(500, "ERROR", this.message!!, null)
            baseActivity.error(errorBody)
        }
    }
}

suspend fun <T> ResponseHandler<T>.handleResponse(baseViewModel: BaseViewModel, onSuccess: suspend (T) -> Unit) {
    when (this) {
        is ResponseHandler.Loading -> baseViewModel.loading.send(this.loading)
        is ResponseHandler.Error -> baseViewModel.error.send(this.errorBody!!)
        is ResponseHandler.Failure -> baseViewModel.failure.send(this.exception ?: Throwable(message = "Unknown error occurred"))
        is ResponseHandler.Success -> {
            baseViewModel.loading.send(false)
            if (this.result != null) {
                onSuccess(this.result)
            }
        }
    }
}