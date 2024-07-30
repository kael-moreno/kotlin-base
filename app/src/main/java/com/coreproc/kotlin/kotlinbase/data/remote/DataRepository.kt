package com.coreproc.kotlin.kotlinbase.data.remote

import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import javax.inject.Inject

class DataRepository
@Inject
constructor(
    private val apiInterface: ApiInterface
) : DataSource {

    override suspend fun getSomething(): Resource<SampleResponse> {
        val response = apiInterface.getSomething()
        return if (response.isSuccessful) {
            Resource.success(response.body()!!)
        } else {
            Resource.error(null, ApiError.parseError(response).getFullMessage())
        }
    }
}
