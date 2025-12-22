plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        compileSdk = 36
        namespace = "amu.shared"
    }

    applyDefaultHierarchyTemplate()

    jvmToolchain(libs.versions.java.get().toInt())

    sourceSets {
        commonMain.dependencies {
            api(compose.components.uiToolingPreview)
            api(compose.foundation)
            api(compose.material3)
            api(compose.runtime)
            api(compose.ui)
        }

        commonTest.dependencies {

        }

        androidMain.dependencies {
            api(libs.androidx.activity.compose)
            api(libs.androidx.appcompat)
            api(libs.androidx.core.ktx)
        }
    }
}
