package com.coreproc.kotlin.kotlinbase.di

import android.app.Application
import com.coreproc.kotlin.kotlinbase.App
import com.coreproc.kotlin.kotlinbase.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    FragmentModule::class,
    ViewModelModule::class,
    NetworkClientModule::class,
    NetworkApiModule::class,
    DataSourceModule::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}