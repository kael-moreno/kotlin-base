package com.coreproc.kotlin.kotlinbase.extensions

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object DataExtension {
    // DATE
    fun formatToHumanReadable(date: Date, format: String): String {
        return SimpleDateFormat(format, Locale.getDefault())
            .format(date.time)
    }

    fun formatToHumanReadable(calendar: Calendar, format: String): String {
        return SimpleDateFormat(format, Locale.getDefault())
            .format(calendar.time)
    }

    // AMOUNT
    fun formatToHumanReadableAmount(value: Double): String {
        return "₱" + DecimalFormat("#,###,##0.00").format(value)
    }
}