package com.coreproc.kotlin.kotlinbase.ui.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.coreproc.kotlin.kotlinbase.R
import com.coreproc.kotlin.kotlinbase.data.remote.ErrorBody
import com.coreproc.kotlin.kotlinbase.misc.AppPreferences
import com.coreproc.kotlin.kotlinbase.ui.main.MainActivity
import com.coreproc.kotlin.kotlinbase.utils.DeviceUtilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseComposeActivity : ComponentActivity() {

    @Inject
    lateinit var deviceUtilities: DeviceUtilities

    @Inject
    lateinit var appPreferences: AppPreferences

    protected lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }

    /**
     * Composable function that handles all the base UI states (loading, errors, dialogs).
     * Should be called within the main content of your Compose UI.
     *
     * @param baseViewModel The BaseViewModel to observe states from
     * @param content The main content to display
     */
    @Composable
    protected fun BaseContent(
        baseViewModel: BaseViewModel,
        content: @Composable () -> Unit
    ) {
        BaseContent(baseViewModels = listOf(baseViewModel), content = content)
    }

    /**
     * Composable function that handles all the base UI states (loading, errors, dialogs) for multiple ViewModels.
     * Should be called within the main content of your Compose UI.
     *
     * @param baseViewModels List of BaseViewModels to observe states from
     * @param content The main content to display
     */
    @Composable
    protected fun BaseContent(
        baseViewModels: List<BaseViewModel>,
        content: @Composable () -> Unit
    ) {
        val context = LocalContext.current

        // Collect states from all BaseViewModels and combine them
        val loadingStates = baseViewModels.map { it.loadingStateFlow.collectAsStateWithLifecycle() }
        val errorStates = baseViewModels.map { it.errorStateFlow.collectAsStateWithLifecycle() }
        val failureStates = baseViewModels.map { it.failureStateFlow.collectAsStateWithLifecycle() }
        val unauthorizedStates = baseViewModels.map { it.unauthorizedStateFlow.collectAsStateWithLifecycle() }
        val noInternetConnectionStates = baseViewModels.map { it.noInternetConnectionStateFlow.collectAsStateWithLifecycle() }

        // Combine states - loading if ANY ViewModel is loading, error/failure from the FIRST non-null state
        val isLoading by remember {
            derivedStateOf {
                loadingStates.any { it.value }
            }
        }

        val currentError by remember {
            derivedStateOf {
                errorStates.firstNotNullOfOrNull { it.value }
            }
        }

        val currentFailure by remember {
            derivedStateOf {
                failureStates.firstNotNullOfOrNull { it.value }
            }
        }

        val isUnauthorized by remember {
            derivedStateOf {
                unauthorizedStates.any { it.value }
            }
        }

        val currentNoInternetConnection by remember {
            derivedStateOf {
                noInternetConnectionStates.firstNotNullOfOrNull { it.value }
            }
        }

        // State for dialog management
        var dialogState by remember { mutableStateOf<DialogState?>(null) }

        // Handle error states
        LaunchedEffect(currentError) {
            currentError?.let {
                dialogState = DialogState(
                    title = context.getString(R.string.error),
                    message = it.getFullMessage(),
                    isError = true
                )
            }
        }

        LaunchedEffect(currentFailure) {
            currentFailure?.let {
                dialogState = DialogState(
                    title = context.getString(R.string.error),
                    message = it.message ?: "An unknown error occurred",
                    isError = true
                )
            }
        }

        LaunchedEffect(isUnauthorized) {
            if (isUnauthorized) {
                dialogState = DialogState(
                    title = context.getString(R.string.session_expired),
                    message = context.getString(R.string.please_login_again),
                    isUnauthorized = true
                )
            }
        }

        LaunchedEffect(currentNoInternetConnection) {
            currentNoInternetConnection?.let {
                Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }

        // Main content
        Box(modifier = Modifier.fillMaxSize()) {
            content()

            // Loading overlay
            if (isLoading) {
                LoadingDialog()
            }

            // Error/Dialog handling
            dialogState?.let { state ->
                when {
                    state.isUnauthorized -> {
                        UnauthorizedDialog(
                            title = state.title,
                            message = state.message,
                            onConfirm = {
                                dialogState = null
                                // Clear unauthorized state from all ViewModels
                                baseViewModels.forEach { it.clearUnauthorized() }
                                MainActivity.startActivity(context)
                            }
                        )
                    }
                    else -> {
                        ErrorDialog(
                            title = state.title,
                            message = state.message,
                            onDismiss = {
                                dialogState = null
                                // Clear error/failure state from all ViewModels
                                baseViewModels.forEach {
                                    it.clearError()
                                    it.clearFailure()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadingDialog() {
        Dialog(
            onDismissRequest = { /* Cannot dismiss */ },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    @Composable
    private fun ErrorDialog(
        title: String,
        message: String,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(LocalContext.current.getString(R.string.ok))
                }
            }
        )
    }

    @Composable
    private fun UnauthorizedDialog(
        title: String,
        message: String,
        onConfirm: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { /* Cannot dismiss */ },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(LocalContext.current.getString(R.string.ok))
                }
            }
        )
    }

    data class DialogState(
        val title: String,
        val message: String,
        val isError: Boolean = false,
        val isUnauthorized: Boolean = false
    )
}
