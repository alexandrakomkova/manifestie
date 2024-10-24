
plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)


    alias(libs.plugins.kotlin.serialization) apply false
    id("co.touchlab.skie") version "0.8.4" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0") // Ensure this matches your Android Gradle plugin version
        classpath("com.google.gms:google-services:4.4.2") // Add this line
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
