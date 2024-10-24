import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("co.touchlab.skie")
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.example.manifestie.resources"
    generateResClass = auto
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-annotations-common"))
            implementation(libs.assertk)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.ktor.test)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.bundles.ktor)

            implementation(libs.bundles.glance)
            //implementation(platform("com.google.firebase:firebase-bom:30.0.1"))
            implementation("com.google.gms:google-services:4.4.2")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.bundles.ktor)
            implementation("io.github.aakira:napier:2.7.1")

            implementation(libs.koin.test)
            implementation(libs.koin.compose)
            implementation(libs.koin.core)

            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)

            api(libs.datastore)
            api(libs.datastore.preferences)

            implementation(libs.navigation.compose)

            api(libs.mvvm.core)
            api(libs.mvvm.compose)
            api(libs.mvvm.flow)
            api(libs.mvvm.flow.compose)

            implementation(libs.firebase.firestore)
            implementation(libs.firebase.common)
            implementation(libs.kotlinx.serialization.json)

            //implementation("com.google.android.gms:play-services:17.0.0")
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }
}

android {
    namespace = "com.example.manifestie"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.firestore.ktx)
}

fun getUnsplashAccess(): String? {
    return project.findProperty("unsplash_access_key") as? String
}