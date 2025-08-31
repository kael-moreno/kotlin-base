package com.coreproc.kotlin.kotlinbase.data.remote

import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class DataRepository
@Inject
constructor(
    private val apiInterface: ApiInterface
) : DataSource {

    override suspend fun getSomething(): Flow<ResponseHandler<SampleResponse>> {

        return flow {
            emit(ResponseHandler.Loading(loading = true))
            val response = apiInterface.getSomething()
            emit(ResponseHandler.Loading())

            if (!response.isSuccessful) {
                emit(ResponseHandler.Error(ApiError.parseError(response)))
                return@flow
            }

            emit(ResponseHandler.Success(data = response.body()!!))
        }.catch { ex ->
            emit(ResponseHandler.Loading())
            if (ex is CancellationException)
                throw ex

            Timber.e(ex)

            emit(ResponseHandler.Failure(ex))
        }
    }
}
