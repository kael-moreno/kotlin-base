package com.coreproc.kotlin.kotlinbase.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.ui.base.AppViewModelFactory
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private var viewModel: MainViewModel? = null

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)
        viewModel!!.success.observe(this, Observer { this.onSuccess(it) })

    }

    override fun initialize() {
    }

    fun onSuccess(str: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel!!.success.removeObservers(this)
    }
}
