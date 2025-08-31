package com.coreproc.kotlin.kotlinbase.ui.main

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.databinding.ActivityMainBinding
import com.coreproc.kotlin.kotlinbase.extensions.applyWindowInsets
import com.coreproc.kotlin.kotlinbase.extensions.showShortToast
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun initialize() {
        activityMainBinding = ActivityMainBinding.bind(getChildActivityView())
        applyWindowInsets()
        initObservables()
        initClicks()
    }

    private fun initObservables() {
        viewModel.bindActivity(this)
        viewModel.successFlow.onEach {
            activityMainBinding.helloWorldTextView.text = it.setup
            showShortToast(it.punchline)
        }.launchIn(lifecycleScope)

        viewModel.getSomething()
    }

    private fun initClicks() {
        activityMainBinding.retryButton.setOnClickListener {
            viewModel.getSomething()
        }
    }
}
