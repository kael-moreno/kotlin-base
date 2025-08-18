// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion by extra("2.0.0")
    val gradleVersion by extra("8.7.0")

    repositories {
        google()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

plugins {
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.24" apply false
}

allprojects {
    repositories {
        maven(url = "https://maven.google.com")
        google()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

ext.apply {
    // Version Info
    set("versionMajor", 0)    // backward compatibility
    set("versionMinor", 0)    // new feature or a major behavior change
    set("versionPatch", 1)    // fixes or minor patch
    set("code", 1)

    // SDK and tools
    set("minSdkVersion", 27)
    set("targetSdkVersion", 34)
    set("compileSdkVersion", 34)

    set("crashlyticsVersion", "2.9.5")
    set("multidexVersion", "1.0.3")

    // Timber
    set("timberVersion", "5.0.1")

    // App dependencies
    set("supportLibraryVersion", "1.7.0")
    set("constraintVersion", "2.2.0-alpha13")
    set("playServicesVersion", "16.0.0")
    set("fragmentKtxVersion", "1.8.2")
    set("activityKtxVersion", "1.9.1")

    set("apacheVersion", "3.7")

    set("retrofitVersion", "2.9.0")
    set("gsonVersion", "2.9.1")
    set("okhttpVersion", "4.12.0")

    set("hiltAndroidVersion", "2.51.1")
    set("hiltCompilerVersion", "2.51.1")

    set("glideVersion", "4.14.2")

    // Testing
    set("junitVersion", "4.12")
    set("mockitoVersion", "1.10.19")
    set("mockitoCoreVersion", "2.19.0")
    set("hamcrestVersion", "1.3")

    // Sentry
    set("sentryVersion", "6.16.0")

    // Coroutines
    set("coroutinesVersion", "1.6.1")

    // Lifecycle
    set("lifecycleVersion", "2.8.4")
}
