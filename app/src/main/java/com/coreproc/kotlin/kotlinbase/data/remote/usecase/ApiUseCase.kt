package com.coreproc.kotlin.kotlinbase.data.remote.usecase

import com.coreproc.kotlin.kotlinbase.data.remote.DataSource
import com.coreproc.kotlin.kotlinbase.data.remote.Resource
import com.coreproc.kotlin.kotlinbase.data.remote.UseCase
import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import javax.inject.Inject

class ApiUseCase
@Inject
constructor(private val dataSource: DataSource) : UseCase<String, Resource<SampleResponse>>() {

    override suspend fun executeUseCase(requestValues: String): Resource<SampleResponse> {
        return dataSource.getSomething()
    }

}