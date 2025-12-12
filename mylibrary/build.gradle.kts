plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.mobileads.android)
}

android {
    namespace = "com.kodex.mylibrary"
    compileSdkPreview = "UpsideDownCake"

    defaultConfig {
        minSdkPreview = "UpsideDownCake"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    //implementation("com.yandex.android:mobileads:7.0.0")
   // implementation("com.yandex.android:mobileads-mediation:7.0.0.0")
   // implementation(libs.androidx.mobileads)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.coil.compose)

    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}