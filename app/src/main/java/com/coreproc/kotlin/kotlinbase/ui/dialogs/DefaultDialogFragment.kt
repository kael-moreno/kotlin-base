package com.coreproc.kotlin.kotlinbase.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.coreproc.kotlin.kotlinbase.databinding.DialogDefaultBinding

class DefaultDialogFragment : DialogFragment() {

    private var _binding: DialogDefaultBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var message: String? = null
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var neutralButtonText: String? = null
    private var positiveButtonClickListener: (() -> Unit)? = null
    private var negativeButtonClickListener: (() -> Unit)? = null
    private var neutralButtonClickListener: (() -> Unit)? = null
    private var isCancelable: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
    }

    private fun setupDialog() {
        // Set title
        title?.let {
            binding.textViewTitle.text = it
            binding.textViewTitle.isVisible = true
        }

        // Set message
        message?.let {
            binding.textViewMessage.text = it
            binding.textViewMessage.isVisible = true
        }

        // Set positive button
        positiveButtonText?.let { text ->
            binding.buttonPositive.text = text
            binding.buttonPositive.isVisible = true
            binding.buttonPositive.setOnClickListener {
                positiveButtonClickListener?.invoke()
                dismiss()
            }
        }

        // Set negative button
        negativeButtonText?.let { text ->
            binding.buttonNegative.text = text
            binding.buttonNegative.isVisible = true
            binding.buttonNegative.setOnClickListener {
                negativeButtonClickListener?.invoke()
                dismiss()
            }
        }

        // Set neutral button
        neutralButtonText?.let { text ->
            binding.buttonNeutral.text = text
            binding.buttonNeutral.isVisible = true
            binding.buttonNeutral.setOnClickListener {
                neutralButtonClickListener?.invoke()
                dismiss()
            }
        }

        // Set cancelable
        dialog?.setCancelable(isCancelable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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