plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    //id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.paging3_tvshowsapi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.paging3_tvshowsapi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Paging
    //implementation ("androidx.paging:paging-runtime-ktx:3.3.0")
    implementation (libs.androidx.paging.runtime)


    //Retrofit
    implementation (libs.retrofit)

    //Retrofit converters
    implementation(libs.retrofit.converter.gson)
    //implementation(libs.converter.scalars)
    // Moshi
    //implementation (libs.moshi.kotlin)
    //implementation (libs.converter.moshi)

    implementation(libs.okhttp.logging.interceptor)

    //Hilt
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.hilt.android.compiler)



    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v1)

    // to make kotlin-dsl red underline dissappear
    implementation(kotlin("script-runtime"))

    // coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Kotlin
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //glide
    implementation (libs.glide)

    //gradle.kts nonsense error
    kapt (libs.compiler)

    //room

    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)




// swiperefreshlayout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

}



// Allow references to generated code
kapt {
    correctErrorTypes = true
}

