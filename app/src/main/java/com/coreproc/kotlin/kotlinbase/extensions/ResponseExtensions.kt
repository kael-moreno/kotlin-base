package com.coreproc.kotlin.kotlinbase.extensions

import com.coreproc.kotlin.kotlinbase.data.remote.ApiError
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.NotYetConnectedException
import javax.net.ssl.SSLHandshakeException

fun <T : Any> Response<T>.postErrorBody(baseViewModel: BaseViewModel) {
    baseViewModel.loading.postValue(false)
    if (!this.isSuccessful) {
        baseViewModel.error.postValue(ApiError.parseError(this))
        return
    }
    baseViewModel.error.postValue(null)
}

fun Throwable.postError(baseViewModel: BaseViewModel) {
    baseViewModel.loading.postValue(false)
    if (this is NotYetConnectedException || this is UnknownHostException || this is SocketTimeoutException
        || this is ConnectionShutdownException || this is IOException
        || this is SSLHandshakeException
    ) {
        baseViewModel.noInternetConnection.postValue(this)
        return
    }

    val errorBody = ErrorBody(500, "ERROR", this.message!!, null)
    baseViewModel.error.postValue(errorBody)
}

fun <T : Any> Response<T>.postErrorBody(message: String, baseViewModel: BaseViewModel) {
    baseViewModel.loading.postValue(false)
    if (this.isSuccessful) {
        baseViewModel.error.postValue(ErrorBody(500, "Error", message, null))
        return
    }

    val errorBody = ErrorBody(500, "ERROR", "An error occurred", null)
    baseViewModel.error.postValue(errorBody)
}
