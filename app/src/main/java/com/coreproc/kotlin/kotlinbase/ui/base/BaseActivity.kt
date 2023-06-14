package com.coreproc.kotlin.kotlinbase.ui.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.databinding.ActivityBaseLayoutBinding
import com.coreproc.kotlin.kotlinbase.databinding.DefaultToolbarBinding
import com.coreproc.kotlin.kotlinbase.extensions.setVisible
import com.coreproc.kotlin.kotlinbase.extensions.showShortToast
import com.coreproc.kotlin.kotlinbase.utils.DeviceUtilities
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var deviceUtilities: DeviceUtilities

    private lateinit var baseActivityBinding: ActivityBaseLayoutBinding

    private lateinit var defaultToolbarBinding: DefaultToolbarBinding

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initialize()

    protected lateinit var context: Context

    private lateinit var viewStubView: View

    private var defaultToolbar: Toolbar? = null

    private var listOfViewModels = mutableListOf<BaseViewModel>()

    override fun setTitle(titleId: Int) {
        defaultToolbarBinding.toolbarTitleTextView.setText(titleId)
        supportActionBar!!.title = ""
    }

    override fun setTitle(title: CharSequence) {
        defaultToolbarBinding.toolbarTitleTextView.text = title
        supportActionBar!!.title = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        baseActivityBinding = ActivityBaseLayoutBinding.inflate(layoutInflater)
        defaultToolbarBinding = baseActivityBinding.toolbarLayout
        setContentView(baseActivityBinding.root)
        initUi()
    }

    override fun onDestroy() {
        listOfViewModels.forEach {
            it.removeObservers(this)
        }
        super.onDestroy()
    }


    @SuppressLint("ClickableViewAccessibility")
    fun initUi() {
        defaultToolbar = defaultToolbarBinding.defaultToolbar
        setSupportActionBar(defaultToolbar)
        defaultToolbarBinding.toolbarTitleTextView.text = supportActionBar?.title
        setTitle(title)
        baseActivityBinding.loadingDialogRelativeLayout.setOnTouchListener { _, _ -> true }

        initViewStub()
        initialize()
    }

    private fun initViewStub() {
        baseActivityBinding.baseViewStub.layoutResource = getLayoutResource()
        baseActivityBinding.baseViewStub.setOnInflateListener { _, inflated ->
            viewStubView = inflated
        }
        baseActivityBinding.baseViewStub.inflate()
    }

    protected fun getChildActivityView(): View = viewStubView

    fun <T: BaseViewModel> initViewModel(viewModelClass: Class<T>): T {
        val viewModel = ViewModelProvider(this)[viewModelClass]
        viewModel.observeCommonEvent(this)
        listOfViewModels.add(viewModel)
        return viewModel
    }


    fun hideToolbar() {
        if (defaultToolbar != null)
            defaultToolbarBinding.parentToolbarRelativeLayout.visibility = View.GONE
    }

    fun setToolbar(toolbar: Toolbar) {
        hideToolbar()
        defaultToolbar = toolbar
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE
    }

    fun setCustomToolbarMenuItem(resourceId: Int, onClickListener: View.OnClickListener) {
        defaultToolbarBinding.rightImageView.setImageResource(resourceId)
        defaultToolbarBinding.rightImageView.setOnClickListener(onClickListener)
        defaultToolbarBinding.rightImageView.visibility = View.VISIBLE
    }

    fun setCustomToolbarMenuItemVisibility(visibility: Int) {
        defaultToolbarBinding.rightImageView.visibility = visibility
    }

    fun getCustomToolbarMenuItem(): AppCompatImageView {
        return defaultToolbarBinding.rightImageView
    }

    fun showDefaultDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.ok), null)
        builder.create().show()
    }

    fun showDefaultDialog(
        title: String, message: String,
        onClickListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.ok), onClickListener)
        builder.create().show()
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

    open fun showDefaultErrorDialog(message: String) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok), null)
            .create().show()
    }

    open fun buildDefaultDialog(
        title: String?, message: String?,
        okButton: String,
        onClickListener: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        if (title != null && title.isNotEmpty()) builder.setTitle(title)
        if (message != null && message.isNotEmpty()) builder.setMessage(message)

        builder.setCancelable(false)
        builder.setPositiveButton(okButton, onClickListener)
        return builder
    }

    fun getDeviceUtil() : DeviceUtilities {
        return deviceUtilities
    }

    open fun noInternetConnection(throwable: Throwable) {
        throwable.printStackTrace()
        showShortToast(getString(R.string.an_error_occurred))
    }

    open fun loading(it: Boolean) {
        baseActivityBinding.loadingDialogRelativeLayout.setVisible(it)
    }

    open fun error(it: ErrorBody) {
        showDefaultErrorDialog(it.getFullMessage())
    }

    open fun unauthorized(boolean: Boolean) {
        buildDefaultDialog(null, getString(R.string.session_expired), getString(R.string.ok)
        ) { _, _ ->

        }
    }

}