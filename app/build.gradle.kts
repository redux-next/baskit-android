plugins {
    alias(libs.plugins.android.application)
}

val releaseStoreFile = providers.gradleProperty("DAILYCART_RELEASE_STORE_FILE")
    .orElse(providers.gradleProperty("BASKIT_RELEASE_STORE_FILE")).orNull
val releaseStorePassword = providers.gradleProperty("DAILYCART_RELEASE_STORE_PASSWORD")
    .orElse(providers.gradleProperty("BASKIT_RELEASE_STORE_PASSWORD")).orNull
val releaseKeyAlias = providers.gradleProperty("DAILYCART_RELEASE_KEY_ALIAS")
    .orElse(providers.gradleProperty("BASKIT_RELEASE_KEY_ALIAS")).orNull
val releaseKeyPassword = providers.gradleProperty("DAILYCART_RELEASE_KEY_PASSWORD")
    .orElse(providers.gradleProperty("BASKIT_RELEASE_KEY_PASSWORD")).orNull

android {
    namespace = "com.getbaskit.customer"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.getbaskit.customer"
        minSdk = 23
        targetSdk = 37

        versionCode = 3
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (!releaseStoreFile.isNullOrBlank()) {
                storeFile = file(releaseStoreFile)
            }

            storePassword = releaseStorePassword
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
        }
    }

    buildTypes {
        release {
            if (!releaseStoreFile.isNullOrBlank()) {
                signingConfig = signingConfigs.getByName("release")
            }

            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}
