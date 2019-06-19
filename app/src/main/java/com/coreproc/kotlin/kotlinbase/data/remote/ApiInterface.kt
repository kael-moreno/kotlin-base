package com.coreproc.kotlin.kotlinbase.data.remote

import io.reactivex.Observable
import retrofit2.http.GET

interface ApiInterface {

    @GET("url here")
    fun getSomething(): Observable<String>
}
