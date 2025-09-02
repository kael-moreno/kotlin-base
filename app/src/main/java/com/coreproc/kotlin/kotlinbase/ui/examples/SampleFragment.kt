package com.coreproc.kotlin.kotlinbase.ui.examples

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.coreproc.kotlin.kotlinbase.databinding.FragmentSampleBinding
import com.coreproc.kotlin.kotlinbase.ui.base.BaseFragment

class SampleFragment : BaseFragment() {

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return FragmentSampleBinding.inflate(inflater, container, false)
    }

    override fun initialize() {
        val binding = getBinding<FragmentSampleBinding>()
        setupViews(binding)
        setupListeners(binding)
    }

    private fun setupViews(binding: FragmentSampleBinding) {
        binding.textViewTitle.text = "Welcome to Sample Fragment"
        binding.editTextInput.hint = "Type something here..."
    }

    private fun setupListeners(binding: FragmentSampleBinding) {
        binding.buttonSubmit.setOnClickListener {
            val inputText = binding.editTextInput.text.toString()

            if (inputText.isNotEmpty()) {
                binding.textViewResult.text = "You entered: $inputText"
                binding.editTextInput.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
