
plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)


    alias(libs.plugins.kotlin.serialization) apply false
    id("co.touchlab.skie") version "0.8.4" apply false
    id ("dev.icerock.mobile.multiplatform-resources") version "0.24.2" apply false
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.24.2")
        classpath("app.cash.sqldelight:gradle-plugin:2.0.1")
    }
}
