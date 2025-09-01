package com.coreproc.kotlin.kotlinbase.ui.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.databinding.ActivityBaseLayoutBinding
import com.coreproc.kotlin.kotlinbase.databinding.DefaultToolbarBinding
import com.coreproc.kotlin.kotlinbase.extensions.setVisible
import com.coreproc.kotlin.kotlinbase.extensions.showDefaultDialog
import com.coreproc.kotlin.kotlinbase.extensions.showDefaultErrorDialog
import com.coreproc.kotlin.kotlinbase.extensions.showShortToast
import com.coreproc.kotlin.kotlinbase.misc.AppPreferences
import com.coreproc.kotlin.kotlinbase.ui.main.MainActivity
import com.coreproc.kotlin.kotlinbase.utils.DeviceUtilities
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : FragmentActivity() {

    @Inject
    lateinit var deviceUtilities: DeviceUtilities

    @Inject
    lateinit var appPreferences: AppPreferences

    private var baseActivityBinding: ActivityBaseLayoutBinding? = null
    private val _baseActivityBinding = baseActivityBinding!!

    private var defaultToolbarBinding: DefaultToolbarBinding? = null
    private val _defaultToolbarBinding = defaultToolbarBinding!!

    private var defaultToolbar: Toolbar? = null
    private var _defaultToolbar = defaultToolbar!!

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initialize()

    protected lateinit var context: Context

    private var viewStubView: View? = null
    private val _viewStubView = viewStubView!!


    override fun setTitle(titleId: Int) {
        _defaultToolbarBinding.defaultToolbar.title = getString(titleId)
    }

    override fun setTitle(title: CharSequence) {
        _defaultToolbarBinding.defaultToolbar.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        baseActivityBinding = ActivityBaseLayoutBinding.inflate(layoutInflater)
        defaultToolbarBinding = _baseActivityBinding.toolbarLayout
        setContentView(_baseActivityBinding.root)
        initUi()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initUi() {
        defaultToolbar = _defaultToolbarBinding.defaultToolbar
        _baseActivityBinding.loadingDialogRelativeLayout.setOnTouchListener { _, _ -> true }

        initViewStub()
    }

    private fun initViewStub() {
        _baseActivityBinding.baseViewStub.layoutResource = getLayoutResource()
        _baseActivityBinding.baseViewStub.setOnInflateListener { _, inflated ->
            viewStubView = inflated
            initialize()
        }
        _baseActivityBinding.baseViewStub.inflate()
    }

    protected fun getChildActivityView(): View = _viewStubView

    fun hideToolbar() {
        _defaultToolbar.isVisible = false
    }

    fun setToolbar(toolbar: Toolbar) {
        hideToolbar()
        _defaultToolbar = toolbar
        _defaultToolbar.isVisible = true
    }

    fun initDefaultRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    fun initGridRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, spanCount: Int) {
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        gridLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }

    open fun noInternetConnection(throwable: Throwable) {
        throwable.printStackTrace()
        showShortToast(getString(R.string.no_internet_connection))
    }

    open fun loading(it: Boolean) {
        _baseActivityBinding.loadingDialogRelativeLayout.setVisible(it)
    }

    open fun error(it: ErrorBody) {
        showDefaultErrorDialog(it.getFullMessage())
    }

    open fun unauthorized() {
        showDefaultDialog(getString(R.string.session_expired),
            getString(R.string.please_login_again)) {
            MainActivity.startActivity(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear view binding references to prevent memory leaks
        baseActivityBinding = null
        defaultToolbarBinding = null
        defaultToolbar = null
        viewStubView = null
    }

}
