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

    private var _baseActivityBinding: ActivityBaseLayoutBinding? = null
    private val baseActivityBinding get() = _baseActivityBinding!!

    private var _defaultToolbarBinding: DefaultToolbarBinding? = null
    private val defaultToolbarBinding get() = _defaultToolbarBinding!!

    private var _defaultToolbar: Toolbar? = null
    private val defaultToolbar get() = _defaultToolbar!!

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initialize()

    protected lateinit var context: Context

    private var _viewStubView: View? = null
    private val viewStubView get() = _viewStubView!!


    override fun setTitle(titleId: Int) {
        defaultToolbarBinding.defaultToolbar.title = getString(titleId)
    }

    override fun setTitle(title: CharSequence) {
        defaultToolbarBinding.defaultToolbar.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        _baseActivityBinding = ActivityBaseLayoutBinding.inflate(layoutInflater)
        _defaultToolbarBinding = baseActivityBinding.toolbarLayout
        setContentView(baseActivityBinding.root)
        initUi()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initUi() {
        _defaultToolbar = defaultToolbarBinding.defaultToolbar
        baseActivityBinding.loadingDialogRelativeLayout.setOnTouchListener { _, _ -> true }

        initViewStub()
    }

    private fun initViewStub() {
        baseActivityBinding.baseViewStub.layoutResource = getLayoutResource()
        baseActivityBinding.baseViewStub.setOnInflateListener { _, inflated ->
            _viewStubView = inflated
            initialize()
        }
        baseActivityBinding.baseViewStub.inflate()
    }

    protected fun getChildActivityView(): View = viewStubView

    fun hideToolbar() {
        defaultToolbar.isVisible = false
    }

    fun setToolbar(toolbar: Toolbar) {
        hideToolbar()
        _defaultToolbar = toolbar
        defaultToolbar.isVisible = true
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
        baseActivityBinding.loadingDialogRelativeLayout.setVisible(it)
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
        _baseActivityBinding = null
        _defaultToolbarBinding = null
        _defaultToolbar = null
        _viewStubView = null
    }

}
