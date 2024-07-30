package com.coreproc.kotlin.kotlinbase.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
abstract class FragmentModule {

    /**
     *  Use this for fragment scoped dependencies
     */

    //    @FragmentScoped
    //    @Provides
    //    fun provideFragmentWithDependency(some: SomeClass)
    //            = FragmentWithDependency(some)

}