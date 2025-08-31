package com.coreproc.kotlin.kotlinbase.extensions

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


// DATE
fun Date.formatToHumanReadable(format: String): String {
    return SimpleDateFormat(format, Locale.getDefault())
        .format(this.time)
}

fun Calendar.formatToHumanReadable(format: String): String {
    return SimpleDateFormat(format, Locale.getDefault())
        .format(this.time)
}

// AMOUNT
fun Double.formatToHumanReadableAmount(): String {
    return "₱" + DecimalFormat("#,###,##0.00").format(this)
}