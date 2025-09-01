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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.databinding.ActivityBaseLayoutBinding
import com.coreproc.kotlin.kotlinbase.databinding.DefaultToolbarBinding
import com.coreproc.kotlin.kotlinbase.extensions.setVisible
import com.coreproc.kotlin.kotlinbase.extensions.showShortToast
import com.coreproc.kotlin.kotlinbase.misc.AppPreferences
import com.coreproc.kotlin.kotlinbase.utils.DeviceUtilities
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var deviceUtilities: DeviceUtilities

    @Inject
    lateinit var appPreferences: AppPreferences

    private lateinit var baseActivityBinding: ActivityBaseLayoutBinding

    private lateinit var defaultToolbarBinding: DefaultToolbarBinding

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initialize()

    protected lateinit var context: Context

    private lateinit var viewStubView: View

    private var defaultToolbar: Toolbar? = null

    override fun setTitle(titleId: Int) {
        defaultToolbarBinding.defaultToolbar.title = getString(titleId)
    }

    override fun setTitle(title: CharSequence) {
        defaultToolbarBinding.defaultToolbar.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        baseActivityBinding = ActivityBaseLayoutBinding.inflate(layoutInflater)
        defaultToolbarBinding = baseActivityBinding.toolbarLayout
        setContentView(baseActivityBinding.root)
        initUi()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initUi() {
        defaultToolbar = defaultToolbarBinding.defaultToolbar
        baseActivityBinding.loadingDialogRelativeLayout.setOnTouchListener { _, _ -> true }

        initViewStub()
    }

    private fun initViewStub() {
        baseActivityBinding.baseViewStub.layoutResource = getLayoutResource()
        baseActivityBinding.baseViewStub.setOnInflateListener { _, inflated ->
            viewStubView = inflated
            initialize()
        }
        baseActivityBinding.baseViewStub.inflate()
    }

    protected fun getChildActivityView(): View = viewStubView

    fun hideToolbar() {
        defaultToolbar?.isVisible = false
    }

    fun setToolbar(toolbar: Toolbar) {
        hideToolbar()
        defaultToolbar = toolbar
        defaultToolbar?.isVisible = true
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

    fun showDefaultDialog(title: String, message: String) {
        buildDefaultDialog(
            title = title,
            message = message,
            okButton = getString(R.string.ok),
            onOkClick = { }
        ).create().show()
    }

    fun showDefaultDialog(
        title: String, message: String,
        onOkClick: () -> Unit
    ) {
        buildDefaultDialog(
            title = title,
            message = message,
            okButton = getString(R.string.ok),
            onOkClick = onOkClick
        ).create().show()
    }

    open fun showDefaultErrorDialog(message: String) {
        buildDefaultDialog(
            title = getString(R.string.error),
            message = message,
            okButton = getString(R.string.ok),
            onOkClick = { /* Do nothing */ }
        ).create().show()
    }

    open fun buildDefaultDialog(
        title: String?, message: String?,
        okButton: String,
        onOkClick: () -> Unit,
        cancelButton: String? = null,
        onCancelClick: (() -> Unit)? = null
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)

        if (!title.isNullOrEmpty()) builder.setTitle(title)
        if (!message.isNullOrEmpty()) builder.setMessage(message)

        builder.setPositiveButton(okButton) { dialog, _ ->
            try {
                onOkClick()
                dialog.dismiss()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        if (!cancelButton.isNullOrEmpty() && onCancelClick != null) {
            builder.setNegativeButton(cancelButton) { dialog, _ ->
                try {
                    onCancelClick()
                    dialog.dismiss()
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
        return builder
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
        buildDefaultDialog(
            null, getString(R.string.session_expired),
            getString(R.string.ok),
            onOkClick = {
                showShortToast("Session expired. Please login again.")
            }
        ).create().show()
    }

}
