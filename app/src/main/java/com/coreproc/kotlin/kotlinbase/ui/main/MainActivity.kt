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
import kotlinx.coroutines.launch

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

        // Example usage of AppPreferences
    }

    private fun initObservables() {
        viewModel.bindActivity(this)
        viewModel.successFlow.onEach {
            activityMainBinding.helloWorldTextView.text = it.setup
            showDefaultDialog(getString(R.string.success), it.punchline)

            demonstrateAppPreferences()
        }.launchIn(lifecycleScope)

        // Example of observing changes reactively
        appPreferences.hasApiKeyFlow().onEach { hasKey ->
            var testString = activityMainBinding.helloWorldTextView.text.toString()

            val status = if (hasKey) "Logged In" else "Logged Out"
            testString += "\nUser Status: $status"
            activityMainBinding.helloWorldTextView.text = testString
        }.launchIn(lifecycleScope)

        viewModel.getSomething()
    }

    private fun initClicks() {
        activityMainBinding.retryButton.setOnClickListener {
            viewModel.getSomething()
        }
    }

    private fun demonstrateAppPreferences() {
        var testString = activityMainBinding.helloWorldTextView.text.toString()
        lifecycleScope.launch {
            // Save a sample string to preferences
            appPreferences.saveApiKey("sample_api_key_12345")
            testString += "\nAPI Key saved to DataStore!"

            // Retrieve and display the string
            val savedApiKey = appPreferences.getApiKey()
            testString += "\nRetrieved API Key: $savedApiKey"

            // Check if API key exists
            if (appPreferences.hasApiKey()) {
                testString += "\nUser has API Key - logged in!"
            }

            activityMainBinding.helloWorldTextView.text = testString
        }

    }
}
