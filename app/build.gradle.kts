import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

// Read the Google Maps API key from local.properties (never committed).
// Falls back to a placeholder so the project still builds without a key;
// the map simply renders blank until a real key is supplied.
val mapsApiKey: String = run {
    val props = Properties()
    val localProps = rootProject.file("local.properties")
    if (localProps.exists()) {
        localProps.inputStream().use { props.load(it) }
    }
    props.getProperty("MAPS_API_KEY") ?: "YOUR_API_KEY_HERE"
}

android {
    namespace = "com.example.hw2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hw2"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        // Exposed to AndroidManifest.xml as ${MAPS_API_KEY}.
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Lifecycle / ViewModel / Fragment (shared state between the two
    // high-score fragments).
    implementation("androidx.fragment:fragment-ktx:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-ktx:1.9.1")

    // Room (top-10 high scores persistence).
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Coroutines.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Google Maps + device location (high-score locations).
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}
