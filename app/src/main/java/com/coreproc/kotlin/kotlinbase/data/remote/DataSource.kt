package com.coreproc.kotlin.kotlinbase.data.remote

import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import kotlinx.coroutines.flow.Flow

interface DataSource {
    suspend fun getSomething(): Flow<ResponseHandler<SampleResponse>>
}