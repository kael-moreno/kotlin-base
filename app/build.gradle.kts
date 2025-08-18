plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

val SENTRY_DSN: String by project

// Release Info
val RELEASE_KEY_ALIAS: String by project
val RELEASE_KEY_PASSWORD: String by project
val RELEASE_STORE_PASSWORD: String by project
val RELEASE_STORE_FILE: String by project

val APP_NAME: String by project
val BASE_URL_INTERNAL: String by project
val BASE_URL_STAGING: String by project
val BASE_URL_PROD: String by project

android {
    compileSdk = rootProject.extra["compileSdkVersion"].toString().toInt()

    flavorDimensions += "default"
    defaultConfig {
        applicationId = "com.coreproc.kotlin.kotlinbase"
        minSdk = rootProject.extra["minSdkVersion"].toString().toInt()
        targetSdk = rootProject.extra["targetSdkVersion"].toString().toInt()
        versionCode = rootProject.extra["code"].toString().toInt()
        versionName =
                "${rootProject.extra["versionMajor"]}.${rootProject.extra["versionMinor"]}.${rootProject.extra["versionPatch"]}+${rootProject.extra["code"]}"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SENTRY_DSN", SENTRY_DSN)
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    signingConfigs {
        create("release") {
            keyAlias = RELEASE_KEY_ALIAS
            keyPassword = RELEASE_KEY_PASSWORD
            storeFile = file(RELEASE_STORE_FILE)
            storePassword = RELEASE_STORE_PASSWORD
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
            resValue("string", "app_name_variant", "[INTERNAL] $APP_NAME")
            versionName =
                    "${rootProject.extra["versionMajor"]}.${rootProject.extra["versionMinor"]}.${rootProject.extra["versionPatch"]}+${rootProject.extra["code"]}"
            setProperty("archivesBaseName", "kotlin-base-$versionName")
            buildConfigField("String", "HOST", BASE_URL_INTERNAL)
        }
        create("staging") {
            dimension = "default"
            resValue("string", "app_name_variant", "[STAGING] $APP_NAME")
            versionName =
                    "${rootProject.extra["versionMajor"]}.${rootProject.extra["versionMinor"]}.${rootProject.extra["versionPatch"]}+${rootProject.extra["code"]}"
            setProperty("archivesBaseName", "kotlin-base-$versionName")
            buildConfigField("String", "HOST", BASE_URL_STAGING)
        }
        create("production") {
            dimension = "default"
            resValue("string", "app_name_variant", APP_NAME)
            versionName =
                    "${rootProject.extra["versionMajor"]}.${rootProject.extra["versionMinor"]}.${rootProject.extra["versionPatch"]}+${rootProject.extra["code"]}"
            setProperty("archivesBaseName", "kotlin-base-$versionName")
            buildConfigField("String", "HOST", BASE_URL_PROD)
        }
    }

    namespace = "com.coreproc.kotlin.kotlinbase"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    val kotlinVersion: String by rootProject.extra

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")

    // App's dependencies
    implementation("androidx.constraintlayout:constraintlayout:${rootProject.extra["constraintVersion"]}")
    implementation("androidx.appcompat:appcompat:${rootProject.extra["supportLibraryVersion"]}")
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["fragmentKtxVersion"]}")
    implementation("androidx.activity:activity-ktx:${rootProject.extra["activityKtxVersion"]}")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-common:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycleVersion"]}")

    implementation("android.arch.lifecycle:extensions:1.1.1")
    implementation("android.arch.paging:runtime:1.0.1")
    ksp("android.arch.lifecycle:common-java8:1.1.1")

    // Hilt Dependencies
    implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltAndroidVersion"]}")
    ksp("com.google.dagger:hilt-android-compiler:${rootProject.extra["hiltCompilerVersion"]}")

    // Apache Common
    implementation("org.apache.commons:commons-lang3:${rootProject.extra["apacheVersion"]}")

    // Network
    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofitVersion"]}")
    implementation("com.squareup.retrofit2:converter-gson:${rootProject.extra["retrofitVersion"]}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${rootProject.extra["retrofitVersion"]}")
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra["okhttpVersion"]}")
    implementation("com.squareup.okhttp3:logging-interceptor:${rootProject.extra["okhttpVersion"]}")
    implementation("com.google.code.gson:gson:${rootProject.extra["gsonVersion"]}")

    // Glide
    implementation("com.github.bumptech.glide:glide:${rootProject.extra["glideVersion"]}")
    ksp("com.github.bumptech.glide:compiler:${rootProject.extra["glideVersion"]}")

    // Sentry
    implementation("io.sentry:sentry-android:${rootProject.extra["sentryVersion"]}")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["coroutinesVersion"]}")

    // Timber (again, in case you want to keep both)
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")
}
