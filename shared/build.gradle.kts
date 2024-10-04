import org.jetbrains.compose.ExperimentalComposeLibrary


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
        compilations.all {
            kotlinOptions {
                //jvmTarget = "1.8"
                jvmTarget = "11"
            }
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
            implementation(platform("com.google.firebase:firebase-bom:30.0.1"))
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

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha02")

            // implementation(libs.sqldelight.coroutines)

            api(libs.mvvm.core)
            api(libs.mvvm.compose)
            api(libs.mvvm.flow)
            api(libs.mvvm.flow.compose)

            implementation("dev.gitlive:firebase-firestore:1.8.1") // This line
            implementation("dev.gitlive:firebase-common:1.8.1")// This line
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            // implementation(libs.sqldelight.native)
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