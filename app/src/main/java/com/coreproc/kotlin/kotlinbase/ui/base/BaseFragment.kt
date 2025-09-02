package com.coreproc.kotlin.kotlinbase.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected fun <VB : ViewBinding> getBinding(): VB = _binding as VB

    protected abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding
    abstract fun initialize()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createBinding(inflater, container)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}