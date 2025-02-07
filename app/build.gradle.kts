import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.mapSecrets)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.hiltCompiler)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}


android {
    namespace = "com.lucaskempe.cropy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lucaskempe.cropy"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }



    }


    secrets {
        propertiesFileName = "secrets.properties"
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")
    }

    buildTypes {

        debug {

/*            val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
            require(apiKey.isNotEmpty()) {
                "is not set in local.properties"
            }
            buildConfigField("String", "API_KEY", apiKey)*/

            val wssBaseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("WSS_BASE_URL")
            require(wssBaseUrl.isNotEmpty()) {
                "wssurl is not set in local.properties"
            }
            buildConfigField("String", "WSS_BASE_URL", wssBaseUrl)

            val directionsKey: String = gradleLocalProperties(rootDir, providers).getProperty("DIRECTIONS_KEY")
            require(wssBaseUrl.isNotEmpty()) {
                "directions is not set in local.properties"
            }
            buildConfigField("String", "DIRECTIONS_KEY", directionsKey)


            val baseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
            require(baseUrl.isNotEmpty()) {
                "baseurl is not set in local.properties"
            }
            buildConfigField("String", "BASE_URL", baseUrl)

        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val wssBaseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("WSS_BASE_URL")
            require(wssBaseUrl.isNotEmpty()) {
                "is not set in local.properties"
            }
            buildConfigField("String", "WSS_BASE_URL", wssBaseUrl)


            val baseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
            require(baseUrl.isNotEmpty()) {
                "is not set in local.properties"
            }
            buildConfigField("String", "BASE_URL", baseUrl)

/*            val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
            require(baseUrl.isNotEmpty()) {
                "not set in local.properties"
            }
            buildConfigField("String", "API_KEY", apiKey)*/

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
        viewBinding = true
        buildConfig=true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.location)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.graphics.shapes.android)
    implementation(libs.androidx.runtime.livedata)

    //firebase
    implementation (libs.firebase.storage)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    // firebase ui
    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")

    // app check google playstore
    implementation("com.google.firebase:firebase-appcheck-playintegrity")

    // splash
    implementation("androidx.core:core-splashscreen:1.2.0-alpha01")

    // auth
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)


    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    implementation (libs.androidx.navigation.runtime.ktx)

    implementation (libs.play.services.maps.v1802)
    implementation (libs.maps.compose)


    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)

    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(project(":datastore"))
    implementation(project(":entities"))
    implementation(project(":components"))
    implementation(project(":workers"))
    implementation(project(":repositories"))
    implementation(project(":maps"))
    implementation(project(":websocket"))

    // To use Kotlin annotation processing tool (kapt)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.coil.compose)

    // gemini
    implementation(libs.generativeai)

    //startup
    implementation (libs.androidx.startup.runtime)

    implementation( libs.kotlinx.coroutines.test)
    implementation(libs.junit)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.gson)
    implementation(libs.androidx.navigation.compose)
    implementation (libs.androidx.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}