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

object ResponseExtensions {
    fun <T : Any> postErrorBody(response: Response<T>, baseViewModel: BaseViewModel) {
        baseViewModel.loading.postValue(false)
        if (!response.isSuccessful) {
            baseViewModel.error.postValue(ApiError.parseError(response))
            return
        }
        baseViewModel.error.postValue(null)
    }

    fun postError(throwable: Throwable, baseViewModel: BaseViewModel) {
        baseViewModel.loading.postValue(false)
        if (throwable is NotYetConnectedException || throwable is UnknownHostException
            || throwable is SocketTimeoutException || throwable is ConnectionShutdownException
            || throwable is IOException || throwable is SSLHandshakeException
        ) {
            baseViewModel.noInternetConnection.postValue(throwable)
            return
        }

        val errorBody = ErrorBody(500, "ERROR", throwable.message!!, null)
        baseViewModel.error.postValue(errorBody)
    }

    fun <T : Any> postErrorBody(response: Response<T>, message: String, baseViewModel: BaseViewModel) {
        baseViewModel.loading.postValue(false)
        if (response.isSuccessful) {
            baseViewModel.error.postValue(ErrorBody(500, "Error", message, null))
            return
        }

        val errorBody = ErrorBody(500, "ERROR", "An error occurred", null)
        baseViewModel.error.postValue(errorBody)
    }
}
