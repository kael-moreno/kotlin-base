package com.coreproc.kotlin.kotlinbase.extensions

import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.data.remote.ResponseHandler
import com.coreproc.kotlin.kotlinbase.data.remote.ApiError
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.NotYetConnectedException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Extension function to post error states to the BaseViewModel and handle UI updates in BaseActivity.
 * This function manages loading state, no internet connection, unauthorized access, and general errors.
 *
 * @param baseViewModel The ViewModel to update with loading and error states
 * @param baseActivity The Activity to display error messages
 */
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

/**
 * Extension function to handle the ResponseHandler and update the BaseViewModel accordingly.
 * This function manages loading, error, failure, and success states.
 *
 * @param baseViewModel The ViewModel to update with loading, error, and failure states
 * @param onSuccess Callback to handle the successful response data
 */
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

/**
 * Extension function to handle API calls with the standard pattern and custom success handling.
 * This allows you to perform additional operations on successful responses in the repository.
 *
 * @param apiCall Suspend function that makes the API call and returns a Retrofit Response
 * @param onSuccess Optional callback to handle the successful response data before emitting
 * @return Flow of ResponseHandler with the API response data
 */
fun <T> handleApiCall(
    apiCall: suspend () -> Response<T>,
    onSuccess: (suspend (T) -> Unit)? = null
): Flow<ResponseHandler<T>> {
    return flow {
        emit(ResponseHandler.Loading())
        val response = apiCall()

        if (!response.isSuccessful) {
            emit(ResponseHandler.Error(ApiError.parseError(response)))
            return@flow
        }

        val responseBody = response.body()!!

        // Execute custom success handling if provided
        onSuccess?.invoke(responseBody)

        emit(ResponseHandler.Success(data = responseBody))
    }.catch { ex ->
        if (ex is CancellationException)
            throw ex

        Timber.e(ex)
        emit(ResponseHandler.Failure(ex))
    }
}
