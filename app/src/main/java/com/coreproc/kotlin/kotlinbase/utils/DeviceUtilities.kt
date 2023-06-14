package com.coreproc.kotlin.kotlinbase.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.coreproc.kotlin.kotlinbase.App
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DeviceUtilities
    @Inject constructor() {

    private var telephonyManager: TelephonyManager =
        (App.instance!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)

    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
    }

    @SuppressLint("MissingPermission")
    fun getSerialNumber(): String {
        return Build.getSerial()
    }

    @SuppressLint("HardwareIds")
    fun getUuid(): String {
        return Settings.Secure.getString(App.instance!!.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getBatteryLevel(): Int {
        return (App.instance!!.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    @SuppressLint("MissingPermission", "NewApi")
    fun getImei(): String? {
        try {
            for (x in 0..10) return telephonyManager.getImei(x)
            return null
        } catch (ex: Exception) {
            return null
        }
    }

    fun getOsVersion(): String {
        return Build.VERSION.RELEASE
    }

    fun getDeviceModel(): String {
        return Build.MODEL
    }

    fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER
    }

    fun getSecurityPatch(): Date? {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Build.VERSION.SECURITY_PATCH)
    }

    fun getDeviceTimestamp(): Long {
        return System.currentTimeMillis()
    }
}