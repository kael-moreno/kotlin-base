package com.coreproc.kotlin.kotlinbase.data.remote

import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse

interface DataSource {
    suspend fun getSomething(): Resource<SampleResponse>
}