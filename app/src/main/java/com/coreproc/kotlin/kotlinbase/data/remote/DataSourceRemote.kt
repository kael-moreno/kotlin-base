package com.coreproc.kotlin.kotlinbase.data.remote

import io.reactivex.Observable
import javax.inject.Inject

class DataSourceRemote @Inject
constructor(private val api: ApiInterface) : ApiInterface {
    override fun getSomething(): Observable<String> {
        return api.getSomething()
    }

}