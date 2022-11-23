package com.coreproc.kotlin.kotlinbase.di.module


import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class ActivityModule {

    /**
     * Use this for activity scoped dependencies
     */

    //  @ActivityScoped
    //  @Provides
    //  fun provideActivityWithDependency(some: SomeClass)
    //              = ActivityWithDependency(some)

}