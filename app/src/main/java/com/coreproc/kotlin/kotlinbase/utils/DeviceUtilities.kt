package com.coreproc.kotlin.kotlinbase.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import com.coreproc.kotlin.kotlinbase.App
import java.text.SimpleDateFormat
import java.util.*

class DeviceUtilities(val context: Context) {

    private var imei: String? = null
    private var telephonyManager: TelephonyManager =
        (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)

    /**
     * Only use this if needed
     *
    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
    handler()
    return null
    }
    }

    init {
    doAsync {

    //imei = AdvertisingIdClient.getAdvertisingIdInfo(context).id
    }.execute()
    }**/

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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()
        } else {
            Build.UNKNOWN
        }
    }

    @SuppressLint("HardwareIds")
    fun getUuid(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getBatteryLevel(): Int {
        return (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
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

    fun getSecurityPatch(): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Build.VERSION.SECURITY_PATCH)
    }

    @SuppressLint("MissingPermission")
    fun getMacAddress(): String {
        val wifiManager = App.instance!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wInfo = wifiManager.connectionInfo
        return wInfo.macAddress
    }

    fun getDeviceTimestamp(): Long {
        return System.currentTimeMillis()
    }
}