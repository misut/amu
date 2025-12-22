rootProject.name = "amu"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

include(
    ":android-app",
    ":shared"
)
