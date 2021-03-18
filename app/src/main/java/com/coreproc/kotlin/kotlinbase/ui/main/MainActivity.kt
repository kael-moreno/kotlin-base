package com.coreproc.kotlin.kotlinbase.ui.main

import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.databinding.ActivityMainBinding
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private var viewModel: MainViewModel? = null

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun initialize() {

        viewModel = initViewModel(MainViewModel::class.java)
        viewModel!!.success.observe(this, { /* onSuccess(it) */ })

        activityMainBinding = ActivityMainBinding.bind(getChildActivityView())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel!!.success.removeObservers(this)
    }
}
