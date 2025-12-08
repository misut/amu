plugins {
    alias {
        libs.plugins.kotlin.android
        libs.plugins.kotlin.multiplatform
    }

    alias {
        libs.plugins.android.application
    }
}

kotlin {
    androidTarget()
}

android {
    compileSdk = 36
    namespace = "amu"
    defaultConfig {
        applicationId = "amu"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
