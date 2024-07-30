package com.coreproc.kotlin.kotlinbase.di.module

import com.coreproc.kotlin.kotlinbase.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object NetworkAPIModule {

    @Provides
    internal fun provideApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}