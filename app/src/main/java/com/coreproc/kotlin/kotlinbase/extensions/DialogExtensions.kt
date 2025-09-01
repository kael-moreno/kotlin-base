package com.coreproc.kotlin.kotlinbase.extensions

import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.coreproc.kotlin.kotlinbase.R
import timber.log.Timber

fun Fragment.showDefaultDialog(title: String, message: String) {
    requireActivity().showDefaultDialog(title, message)
}

fun Activity.showDefaultDialog(title: String, message: String) {
    showDefaultDialog(title, message) {}
}

fun Activity.showDefaultErrorDialog(message: String) {
    showDefaultDialog(getString(R.string.error), message)
}

fun Activity.showDefaultDialog(
    title: String, message: String,
    onOkClick: () -> Unit
) {
    buildDefaultDialog(
        title = title,
        message = message,
        okButton = getString(R.string.ok),
        onOkClick = onOkClick
    ).create().show()
}

fun Activity.buildDefaultDialog(
    title: String?, message: String?,
    okButton: String,
    onOkClick: () -> Unit,
    cancelButton: String? = null,
    onCancelClick: (() -> Unit)? = null
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(false)

    if (!title.isNullOrEmpty()) builder.setTitle(title)
    if (!message.isNullOrEmpty()) builder.setMessage(message)

    builder.setPositiveButton(okButton) { dialog, _ ->
        try {
            onOkClick()
            dialog.dismiss()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
    if (!cancelButton.isNullOrEmpty() && onCancelClick != null) {
        builder.setNegativeButton(cancelButton) { dialog, _ ->
            try {
                onCancelClick()
                dialog.dismiss()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
    return builder
}