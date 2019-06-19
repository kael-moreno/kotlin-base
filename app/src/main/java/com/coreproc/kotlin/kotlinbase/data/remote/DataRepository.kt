package com.coreproc.kotlin.kotlinbase.data.remote

import io.reactivex.Observable
import javax.inject.Inject

class DataRepository
@Inject
constructor(
    private val apiInterface: ApiInterface
) : DataSource {

    override fun getSomething(): Observable<String> {
        return apiInterface.getSomething()
    }
}
