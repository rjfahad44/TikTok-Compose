plugins {
    id("plugin.android-common")
}

android {
    namespace = "com.puskal.feature.inbox" // Adjust this based on your package structure
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    COMMON_THEME
    COMMON_COMPOSABLE
    DOMAIN
    DATA
    CORE
}