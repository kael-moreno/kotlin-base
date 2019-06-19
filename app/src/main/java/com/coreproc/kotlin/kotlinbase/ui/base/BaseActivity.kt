package com.coreproc.kotlin.kotlinbase.ui.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coreproc.kaching.familyplan.extensions.setVisible
import com.coreproc.kaching.familyplan.extensions.showShortToast
import com.coreproc.kotlin.kotlinbase.App
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.utils.DeviceUtilities
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_base_layout.*
import kotlinx.android.synthetic.main.default_toolbar.*
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initialize()

    protected var context: Context? = null

    protected var view: View? = null

    protected var defaultToolbar: Toolbar? = null

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

        context = this
        defaultToolbar = default_toolbar
        setSupportActionBar(default_toolbar)
        toolbar_title_text_view.text = supportActionBar?.title

        base_view_stub.layoutResource = getLayoutResource()
        view = base_view_stub.inflate()

        setTitle(title)
        loading_dialog_relative_layout.setOnTouchListener { _, _ -> true }

        initialize()
    }


    fun <T: BaseViewModel> initViewModel(viewModelClass: Class<T>): T {
        return ViewModelProviders.of(this, viewModelFactory)
            .get(viewModelClass)
    }


    protected fun hideToolbar() {
        if (parent_toolbar_relative_layout != null && default_toolbar != null)
            parent_toolbar_relative_layout.visibility = View.GONE
    }

    fun setToolbar(toolbar: Toolbar) {
        hideToolbar()
        defaultToolbar = toolbar
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

    fun showDefaultErrorDialog(message: String) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok), null)
            .create().show()
    }

    fun buildDefaultDialog(
        title: String?, message: String?,
        okButton: String,
        onClickListener: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        if (title != null && !title.isEmpty()) builder.setTitle(title)
        if (message != null && !message.isEmpty()) builder.setMessage(message)

        builder.setCancelable(false)
        builder.setPositiveButton(okButton, onClickListener)
        return builder
    }

    fun getDeviceUtil() : DeviceUtilities {
        return App.getDeviceInfo()!!
    }

    open fun noInternetConnection(throwable: Throwable) {
        throwable.printStackTrace()
        showShortToast(getString(R.string.no_internet_connection))
    }

    open fun loading(it: Boolean) {
        loading_dialog_relative_layout.setVisible(it)
    }

    open fun error(it: ErrorBody) {
        showDefaultErrorDialog(it.getFullMessage())
    }

    open fun unathorized(boolean: Boolean) {
        buildDefaultDialog(null, getString(R.string.session_expired), getString(R.string.ok),
            DialogInterface.OnClickListener { _, _ ->

            })
    }

}