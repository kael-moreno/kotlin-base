package com.coreproc.kotlin.kotlinbase.data.remote.usecase

import com.coreproc.kotlin.kotlinbase.data.remote.DataSource
import com.coreproc.kotlin.kotlinbase.data.remote.ResponseHandler
import com.coreproc.kotlin.kotlinbase.data.remote.UseCase
import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiUseCase
@Inject
constructor(private val dataSource: DataSource) : UseCase<String, Flow<ResponseHandler<SampleResponse>>>() {

    override suspend fun executeUseCase(requestValues: String): Flow<ResponseHandler<SampleResponse>> {
        return dataSource.getSomething()
    }

}