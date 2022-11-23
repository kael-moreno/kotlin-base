package com.coreproc.kotlin.kotlinbase.ui.main

import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.databinding.ActivityMainBinding
import com.coreproc.kotlin.kotlinbase.extensions.showShortToast
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun initialize() {

        viewModel = initViewModel(MainViewModel::class.java)
        viewModel.success.observe(this) {
            activityMainBinding.helloWorldTextView.text = it.setup
            showShortToast(it.punchline) }
        viewModel.getSomething(this)

        activityMainBinding = ActivityMainBinding.bind(getChildActivityView())

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.success.removeObservers(this)
    }
}
