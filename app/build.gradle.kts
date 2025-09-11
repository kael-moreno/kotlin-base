plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

// Version Info
val versionMajor = 0    // backward compatibility
val versionMinor = 0    // new feature or a major behavior change
val versionPatch = 1    // fixes or minor patch

// For Bitrise
// See step Change value in file (which replaces code's value with BITRISE_BUILD_NUMBER)
val code = 1

val sentryDsn: String by project

// Release Info
val releaseKeyAlias: String by project
val releaseKeyPassword: String by project
val releaseStorePassword: String by project
val releaseStoreFile: String by project

val appName: String by project
val baseUrlInternal: String by project
val baseUrlStaging: String by project
val baseUrlProd: String by project

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    flavorDimensions += "default"
    defaultConfig {
        applicationId = "com.coreproc.kotlin.kotlinbase"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = code

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SENTRY_DSN", sentryDsn)
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    signingConfigs {
        create("release") {
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
            storeFile = file(releaseStoreFile)
            storePassword = releaseStorePassword
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    productFlavors {
        create("internal") {
            dimension = "default"
            resValue("string", "app_name_variant", "[INTERNAL] $appName")
            versionName = "$versionMajor.$versionMinor.$versionPatch-$code"
            setProperty("archivesBaseName", "kotlin-base-$versionName")
            buildConfigField("String", "HOST", baseUrlInternal)
        }
        create("staging") {
            dimension = "default"
            resValue("string", "app_name_variant", "[STAGING] $appName")
            versionName = "$versionMajor.$versionMinor.$versionPatch-$code"
            setProperty("archivesBaseName", "kotlin-base-$versionName")
            buildConfigField("String", "HOST", baseUrlStaging)
        }
        create("production") {
            dimension = "default"
            resValue("string", "app_name_variant", appName)
            versionName = "$versionMajor.$versionMinor.$versionPatch-$code"
            setProperty("archivesBaseName", "kotlin-base-$versionName")
            buildConfigField("String", "HOST", baseUrlProd)
        }
    }

    namespace = "com.coreproc.kotlin.kotlinbase"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)

    // Timber
    implementation(libs.timber)

    // Compose BOM - manages all Compose library versions
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.navigation)
    implementation(libs.compose.hilt.navigation)

    // Compose debugging tools
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // App's dependencies
    implementation(libs.androidx.constraintlayout)
    // implementation(libs.androidx.appcompat) // Removed for full Material 3 migration
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)

    // Lifecycle
    implementation(libs.lifecycle.common)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(libs.arch.lifecycle.extensions)
    implementation(libs.arch.paging.runtime)
    ksp(libs.arch.lifecycle.common.java8)

    // Hilt Dependencies
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Apache Common
    implementation(libs.apache.commons.lang3)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.gson)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Material 3
    implementation(libs.material)

    // DataStore for secure preferences (modern replacement for EncryptedSharedPreferences)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
}
