plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.manifestie.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.manifestie.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        //jvmTarget = "1.8"
        jvmTarget = "11"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)


    implementation(libs.koin.android)
    // implementation(libs.koin.android.compose)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.core)

    implementation(libs.bundles.glance)

    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
}

apply(plugin = "com.google.gms.google-services")