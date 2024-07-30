package com.coreproc.kotlin.kotlinbase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.sentry.android.core.SentryAndroid
import timber.log.Timber

@HiltAndroidApp
class App : Application(){

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        initTimber()
        initSentry()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }


    private fun initSentry() {
        if (BuildConfig.SENTRY_DSN.isNotEmpty()) {
            SentryAndroid.init(this) {
                it.dsn = BuildConfig.SENTRY_DSN
                it.environment = BuildConfig.FLAVOR
            }
        }

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}