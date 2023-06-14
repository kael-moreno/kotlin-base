package com.coreproc.kotlin.kotlinbase.di.module

import com.coreproc.kotlin.kotlinbase.data.remote.DataRepository
import com.coreproc.kotlin.kotlinbase.data.remote.DataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    internal abstract fun provideDataRepository(dataRepository: DataRepository): DataSource
}