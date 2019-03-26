package com.coreproc.kotlin.kotlinbase.data.remote.usecase

import com.coreproc.kotlin.kotlinbase.data.remote.DataSource
import com.coreproc.kotlin.kotlinbase.data.remote.UseCase
import io.reactivex.Observable
import javax.inject.Inject

class ApiUseCase
@Inject
constructor(private val dataSource: DataSource) : UseCase<String, String>() {

    override fun executeUseCase(requestValues: String): Observable<String> {
        return dataSource.getSomething();
    }

}