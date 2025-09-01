package com.coreproc.kotlin.kotlinbase.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DefaultDialogFragment : DialogFragment() {

    private var title: String? = null
    private var message: String? = null
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var neutralButtonText: String? = null
    private var positiveButtonClickListener: (() -> Unit)? = null
    private var negativeButtonClickListener: (() -> Unit)? = null
    private var neutralButtonClickListener: (() -> Unit)? = null
    private var isCancelable: Boolean = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        title?.let { builder.setTitle(it) }
        message?.let { builder.setMessage(it) }

        positiveButtonText?.let { text ->
            builder.setPositiveButton(text) { dialog, _ ->
                positiveButtonClickListener?.invoke()
                dialog.dismiss()
            }
        }

        negativeButtonText?.let { text ->
            builder.setNegativeButton(text) { dialog, _ ->
                negativeButtonClickListener?.invoke()
                dialog.dismiss()
            }
        }

        neutralButtonText?.let { text ->
            builder.setNeutralButton(text) { dialog, _ ->
                neutralButtonClickListener?.invoke()
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.setCancelable(isCancelable)
        return dialog
    }

    class Builder {
        private val fragment = DefaultDialogFragment()

        fun setTitle(title: String) = apply {
            fragment.title = title
        }

        fun setMessage(message: String) = apply {
            fragment.message = message
        }

        fun setPositiveButton(text: String, listener: (() -> Unit)? = null) = apply {
            fragment.positiveButtonText = text
            fragment.positiveButtonClickListener = listener
        }

        fun setNegativeButton(text: String, listener: (() -> Unit)? = null) = apply {
            fragment.negativeButtonText = text
            fragment.negativeButtonClickListener = listener
        }

        fun setNeutralButton(text: String, listener: (() -> Unit)? = null) = apply {
            fragment.neutralButtonText = text
            fragment.neutralButtonClickListener = listener
        }

        fun setCancelable(cancelable: Boolean) = apply {
            fragment.isCancelable = cancelable
        }

        fun build(): DefaultDialogFragment = fragment
    }

    companion object {
        fun newBuilder() = Builder()

        // Convenience method for simple dialogs
        fun showSimpleDialog(
            title: String,
            message: String,
            positiveButtonText: String = "OK",
            onPositiveClick: (() -> Unit)? = null
        ): DefaultDialogFragment {
            return newBuilder()
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, onPositiveClick)
                .build()
        }

        // Convenience method for confirmation dialogs
        fun showConfirmationDialog(
            title: String,
            message: String,
            positiveButtonText: String = "Yes",
            negativeButtonText: String = "No",
            onPositiveClick: (() -> Unit)? = null,
            onNegativeClick: (() -> Unit)? = null
        ): DefaultDialogFragment {
            return newBuilder()
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, onPositiveClick)
                .setNegativeButton(negativeButtonText, onNegativeClick)
                .build()
        }
    }
}