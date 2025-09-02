package com.coreproc.kotlin.kotlinbase.ui.examples

import android.widget.Toast
import com.coreproc.kotlin.kotlinbase.databinding.FragmentSampleBinding
import com.coreproc.kotlin.kotlinbase.ui.base.BaseFragment

class SampleFragment : BaseFragment<FragmentSampleBinding>(FragmentSampleBinding::inflate) {

    override fun initialize() {
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.textViewTitle.text = "Welcome to Sample Fragment"
        binding.editTextInput.hint = "Type something here..."
    }

    private fun setupListeners() {
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
