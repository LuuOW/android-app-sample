import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.fir.declarations.builder.buildScript

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt") // For Kotlin annotation processing
    // secrets
    alias(libs.plugins.mapSecrets)
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

            val glApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("glApiKey")
            require(glApiKey.isNotEmpty()) {
                "glApiKey is not set in local.properties"
            }
            buildConfigField("String", "GL_API_KEY", glApiKey)

        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val glApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("glApiKey")
            require(glApiKey.isNotEmpty()) {
                "glApiKey is not set in local.properties"
            }
            buildConfigField("String", "GL_API_KEY", glApiKey)
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation (libs.androidx.activity.compose.v140)
    implementation(libs.androidx.datastore.core)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.location)
    val room_version = "2.6.1"
    val work_version = "2.9.0"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation ("androidx.navigation:navigation-runtime-ktx:2.7.7")

    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.maps.android:maps-compose:4.3.3")


    val camerax_version = "1.2.2"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")

    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore:1.0.0")





    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.work:work-runtime-ktx:$work_version")
    implementation("io.coil-kt:coil-compose:2.6.0")

    // gemini
    implementation("com.google.ai.client.generativeai:generativeai:0.3.0")

    //startup
    implementation ("androidx.startup:startup-runtime:1.1.1")

    testImplementation( libs.mockito.core)
    implementation( "org.mockito:mockito-core:5.7.0")

    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    implementation("junit:junit:4.13.2")

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