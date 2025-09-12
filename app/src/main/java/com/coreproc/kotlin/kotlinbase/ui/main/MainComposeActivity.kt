package com.coreproc.kotlin.kotlinbase.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coreproc.kotlin.kotlinbase.misc.AppPreferences
import com.coreproc.kotlin.kotlinbase.ui.base.BaseComposeActivity
import com.coreproc.kotlin.kotlinbase.ui.theme.KotlinBaseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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

        enableEdgeToEdge()

        setContent {
            KotlinBaseTheme {
                BaseContent(baseViewModels = listOf(viewModel)) {
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

    // Dummy content cards for testing scrolling
    val dummyCards = remember {
        listOf(
            "Features" to "Explore the latest features and functionality of our app.",
            "Settings" to "Customize your preferences and configure app behavior.",
            "Profile" to "Manage your account information and personal details.",
            "Analytics" to "View insights and statistics about your app usage.",
            "Notifications" to "Control how and when you receive app notifications.",
            "Security" to "Manage your security settings and privacy preferences.",
            "Help & Support" to "Get assistance and find answers to common questions.",
            "About" to "Learn more about the app version and development team.",
            "Feedback" to "Share your thoughts and suggestions for improvement.",
            "Updates" to "Stay informed about the latest app updates and changes."
        )
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
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Kotlin Base - Compose") },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF24292F), // Dark background
                    titleContentColor = Color.White,     // White text
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content text
            item {
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
            }

            // Action buttons
            item {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.getSomething()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Get Random Joke")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            if (hasApiKey) {
                                appPreferences.clearApiKey()
                            } else {
                                appPreferences.saveApiKey("sample_api_key_${System.currentTimeMillis()}")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (hasApiKey) "Logout" else "Login")
                }
            }

            // Dummy content cards using LazyColumn's items
            itemsIndexed(dummyCards) { index, (title, description) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    Timber.d("$title action clicked")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Open $title")
                            }

                            OutlinedButton(
                                onClick = {
                                    Timber.d("$title info clicked")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Learn More")
                            }
                        }
                    }
                }
            }

            // Additional dummy sections
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Statistics",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        repeat(5) { statIndex ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Metric ${statIndex + 1}:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${(100..999).random()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (statIndex < 4) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Final spacer for bottom padding
            item {
                Spacer(modifier = Modifier.height(32.dp))
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
