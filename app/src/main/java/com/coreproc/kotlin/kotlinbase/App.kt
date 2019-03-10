package com.coreproc.kotlin.kotlinbase

import com.coreproc.kotlin.kotlinbase.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }


    override fun onCreate() {
        super.onCreate()

    }
}