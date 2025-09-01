// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion by extra("2.2.0")
    val gradleVersion by extra("8.10.1")

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
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
}

allprojects {
    repositories {
        maven(url = "https://maven.google.com")
        google()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}
