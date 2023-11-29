plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id  ("kotlin-kapt")
    id ("kotlin-parcelize")
}



buildscript {
    repositories {
        // other repositories...
        google()
        mavenCentral()
        maven ("https://jitpack.io")
    }
    dependencies{
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    }

}

android {
    namespace = "com.mr2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mr2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")


    // awesome dialog
    //implementation ("com.github.chnouman:AwesomeDialog:1.0.5")
    implementation ("com.google.android.play:core:1.10.3")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.material:material:1.10.0")

    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    //live data and lifecycle
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //noinspection LifecycleAnnotationProcessorWithJava8
    annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.6.2")

    // Room components
    implementation ("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")

    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    androidTestImplementation ("androidx.room:room-testing:2.6.0")

}