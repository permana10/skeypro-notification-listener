plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    signingConfigs {

    create("release") {

        storeFile = file("skeybox.jks")

        storePassword = "skeypro123"

        keyAlias = "skeybox"

        keyPassword = "skeypro123"
    }
}

    namespace = "id.skeypro.soundbox"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.skeypro.soundbox"
        minSdk = 26
        targetSdk = 35

        versionCode = 200
        versionName = "1.1.0"
    }

    buildTypes {

    release {

        isMinifyEnabled = false

        signingConfig =
            signingConfigs.getByName(
                "release"
            )
    }
}

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")

    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation("com.google.android.material:material:1.12.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}