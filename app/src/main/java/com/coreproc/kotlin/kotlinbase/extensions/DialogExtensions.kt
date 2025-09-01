package com.coreproc.kotlin.kotlinbase.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.ui.dialogs.DefaultDialogFragment

fun Fragment.showDefaultDialog(title: String, message: String) {
    (requireActivity() as? FragmentActivity)?.showDefaultDialog(title, message)
}

fun FragmentActivity.showDefaultDialog(title: String, message: String) {
    showDefaultDialog(title, message) {}
}

fun FragmentActivity.showDefaultErrorDialog(message: String) {
    showDefaultDialog(getString(R.string.error), message)
}

fun FragmentActivity.showDefaultDialog(
    title: String, message: String,
    onOkClick: () -> Unit
) {
    buildDefaultDialog(
        title = title,
        message = message,
        okButton = getString(R.string.ok),
        onOkClick = onOkClick
    )
        .build()
        .show(supportFragmentManager, DefaultDialogFragment::class.java.simpleName)
}

fun FragmentActivity.buildDefaultDialog(
    title: String?, message: String?,
    okButton: String,
    onOkClick: () -> Unit,
    cancelButton: String? = null,
    onCancelClick: (() -> Unit)? = null
): DefaultDialogFragment.Builder {

    val builder = DefaultDialogFragment.newBuilder()
    builder.setCancelable(false)

    if (!title.isNullOrEmpty()) builder.setTitle(title)
    if (!message.isNullOrEmpty()) builder.setMessage(message)

    builder.setPositiveButton(okButton) {
        onOkClick()
    }

    if (!cancelButton.isNullOrEmpty() && onCancelClick != null) {
        builder.setNegativeButton(cancelButton) {
            onCancelClick()
        }
    }
    return builder
}