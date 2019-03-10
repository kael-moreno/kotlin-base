package com.coreproc.kotlin.kotlinbase.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.coreproc.kotlin.kotlinbase.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_base_layout.*
import kotlinx.android.synthetic.main.default_toolbar.*

abstract class BaseActivity : DaggerAppCompatActivity() {

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initialize()

    protected var mContext: Context? = null

    protected var mView: View? = null

    protected var mToolbar: Toolbar? = null

    override fun setTitle(titleId: Int) {

        toolbar_title_text_view.setText(titleId)
        supportActionBar!!.title = ""
    }

    override fun setTitle(title: CharSequence) {
        toolbar_title_text_view.text = title
        supportActionBar!!.title = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_layout)

        initUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun initUi() {

        mContext = this
        mToolbar = default_toolbar
        setSupportActionBar(default_toolbar)
        toolbar_title_text_view.text = supportActionBar?.title

        base_view_stub.layoutResource = getLayoutResource()
        mView = base_view_stub.inflate()

        setTitle(title)
        loading_dialog_relative_layout.setOnTouchListener { _, _ -> true }

        initialize()
    }

    protected fun hideToolbar() {
        if (parent_toolbar_relative_layout != null && default_toolbar != null)
            parent_toolbar_relative_layout.visibility = View.GONE
    }

    fun setToolbar(toolbar: Toolbar) {
        hideToolbar()
        mToolbar = toolbar
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE
    }

    fun setCustomToolbarMenuItem(resourceId: Int, onClickListener: View.OnClickListener) {
        right_image_view.setImageResource(resourceId)
        right_image_view.setOnClickListener(onClickListener)
        right_image_view.visibility = View.VISIBLE
    }

    fun setCustomToolbarMenuItemVisibility(visibility: Int) {
        right_image_view.visibility = visibility
    }

    fun getCustomToolbarMenuItem(): AppCompatImageView {
        return right_image_view
    }

}