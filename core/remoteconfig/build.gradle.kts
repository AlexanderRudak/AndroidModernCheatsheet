plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.rudak.androidcheatsheet.core.remoteconfig"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)

    //implementation(libs.androidx.datastore.preferences)
    debugImplementation(libs.androidx.datastore.preferences)

    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
}