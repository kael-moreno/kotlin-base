package com.coreproc.kotlin.kotlinbase.data.remote

import io.reactivex.Observable

interface DataSource {

    fun getSomething(): Observable<String>
}