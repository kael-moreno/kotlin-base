package com.coreproc.kotlin.kotlinbase

import com.coreproc.kotlin.kotlinbase.di.DaggerAppComponent
import com.coreproc.kotlin.kotlinbase.utils.DeviceUtilities
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.sentry.android.core.SentryAndroid

class App : DaggerApplication() {

    companion object {
        var instance: App? = null
        fun getDeviceInfo(): DeviceUtilities? {
            return instance!!.deviceUtil
        }
    }

    var deviceUtil: DeviceUtilities? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        deviceUtil = DeviceUtilities(this)
        initSentry()
    }

    private fun initSentry() {
        if (BuildConfig.SENTRY_DSN.isNotEmpty()) {
            SentryAndroid.init(this) {
                it.dsn = BuildConfig.SENTRY_DSN
                it.environment = BuildConfig.FLAVOR
            }
        }
    }
}