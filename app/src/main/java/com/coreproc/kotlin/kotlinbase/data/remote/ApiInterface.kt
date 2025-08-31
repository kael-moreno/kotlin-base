package com.coreproc.kotlin.kotlinbase.data.remote

import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("random_joke2")
    suspend fun getSomething(): Response<SampleResponse>
}
