package com.coreproc.kotlin.kotlinbase.extensions

import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
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