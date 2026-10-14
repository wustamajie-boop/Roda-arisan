plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "id.arisan.ku"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.arisan.ku"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
