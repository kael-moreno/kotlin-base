package com.coreproc.kotlin.kotlinbase.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coreproc.kotlin.kotlinbase.misc.AppPreferences
import com.coreproc.kotlin.kotlinbase.ui.base.BaseComposeActivity
import com.coreproc.kotlin.kotlinbase.ui.theme.KotlinBaseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : BaseComposeActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainComposeActivity::class.java))
            (context as Activity).finishAffinity()
        }
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinBaseTheme {
                BaseContent(baseViewModel = viewModel) {
                    MainScreen(
                        viewModel = viewModel,
                        appPreferences = appPreferences
                    )
                }
            }
        } // set content

        viewModel.getSomething()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    appPreferences: AppPreferences
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Collect state from flows
    val successResponse by viewModel.successFlow.collectAsStateWithLifecycle(initialValue = null)
    val hasApiKey by appPreferences.hasApiKeyFlow().collectAsStateWithLifecycle(initialValue = false)

    // Local state for building the display text
    var displayText by remember { mutableStateOf("Hello World!") }
    var apiKeyInfo by remember { mutableStateOf("") }
    var showPunchlineDialog by remember { mutableStateOf(false) }
    var punchlineText by remember { mutableStateOf("") }

    // Update display text when success response changes
    LaunchedEffect(successResponse) {
        successResponse?.let { response ->
            displayText = response.setup
            punchlineText = response.punchline
            showPunchlineDialog = true
        }
    }

    LaunchedEffect(hasApiKey) {
        apiKeyInfo = demonstrateAppPreferences(appPreferences)
    }

    // Punchline Dialog
    if (showPunchlineDialog) {
        AlertDialog(
            onDismissRequest = { showPunchlineDialog = false },
            title = { Text("Success") },
            text = { Text(punchlineText) },
            confirmButton = {
                TextButton(onClick = { showPunchlineDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kotlin Base - Compose") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Main content text
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "App Status",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (apiKeyInfo.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = apiKeyInfo,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "User Status: ${if (hasApiKey) "Logged In" else "Logged Out"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (hasApiKey) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            // Retry button
            Button(
                onClick = { viewModel.getSomething() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retry API Call")
            }

            // Demo preferences button
            OutlinedButton(
                onClick = {
                    scope.launch {
                        demonstrateAppPreferences(appPreferences)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Demo App Preferences")
            }
        }
    }
}

private suspend fun demonstrateAppPreferences(
    appPreferences: AppPreferences
): String {
    var info = ""

    // Save a sample string to preferences
    appPreferences.saveApiKey("sample_api_key_12345")
    info += "\nAPI Key saved to DataStore!"

    // Retrieve and display the string
    val savedApiKey = appPreferences.getApiKey()
    info += "\nRetrieved API Key: $savedApiKey"

    // Check if API key exists
    if (appPreferences.hasApiKey()) {
        info += "\nUser has API Key - logged in!"
    }

    return info
}
