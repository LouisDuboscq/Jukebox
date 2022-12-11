plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lduboscq.jukebox"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    implementation("io.insert-koin:koin-android:${rootProject.extra["koin_version"]}")
    implementation("io.insert-koin:koin-androidx-compose:${rootProject.extra["koin_version"]}")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.extra["lifecycle_version"]}")

    implementation(platform("androidx.compose:compose-bom:${rootProject.extra["compose_bom_version"]}"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material:material")
}