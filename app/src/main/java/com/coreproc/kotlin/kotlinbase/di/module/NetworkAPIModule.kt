package com.coreproc.kotlin.kotlinbase.di.module

import com.coreproc.kotlin.kotlinbase.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NetworkApiModule {

    @Provides
    internal fun provideApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}