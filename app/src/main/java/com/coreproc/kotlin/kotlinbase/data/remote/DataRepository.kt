package com.coreproc.kotlin.kotlinbase.data.remote

import io.reactivex.Observable
import javax.inject.Inject

class DataRepository
@Inject
constructor(
    private val remoteStore: DataSourceRemote
) : DataSource {

    override fun getSomething(): Observable<String> {
        return remoteStore.getSomething()
    }
}
