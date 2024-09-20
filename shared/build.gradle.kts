import org.jetbrains.compose.ExperimentalComposeLibrary


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("co.touchlab.skie")
    id ("dev.icerock.mobile.multiplatform-resources")
    id("app.cash.sqldelight")
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.example.manifestie.resources"
    generateResClass = auto
}

multiplatformResources {
    resourcesPackage.set("com.example.manifestie") // required
    resourcesClassName.set("SharedRes") // optional, default MR
    //resourcesVisibility.set(MRVisibility.Internal) // optional, default Public
    iosBaseLocalizationRegion.set("en") // optional, default "en"
    iosMinimalDeploymentTarget.set("9.0") // optional, default "9.0"
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
            export("dev.icerock.moko:resources:0.24.2")
            export("dev.icerock.moko:graphics:0.9.0")
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

            implementation(libs.sqldelight.android)
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

            api(libs.bundles.moko.resources)
            implementation(compose.components.resources)

            implementation(libs.sqldelight.coroutines)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
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
    commonMainApi(libs.mvvm.core)
    commonMainApi(libs.mvvm.compose)
    commonMainApi(libs.mvvm.flow)
    commonMainApi(libs.mvvm.flow.compose)
}

fun getUnsplashAccess(): String? {
    return project.findProperty("unsplash_access_key") as? String
}