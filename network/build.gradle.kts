import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKsp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.lucaskempe.cropy.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig=true
    }

    buildTypes {

        debug {
            val baseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
            require(baseUrl.isNotEmpty()) {
                "baseURL is not set in local.properties"
            }
            buildConfigField("String", "BASE_URL", baseUrl)
        }

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
}

dependencies {
    implementation(project(":entities"))
    implementation(project(":datastore"))

    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.okhttp)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}