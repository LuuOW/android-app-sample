import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt") // For Kotlin annotation processing
    alias(libs.plugins.mapSecrets)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}


fun getLocalProperty(propertyName: String, project: Project): String? {
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        val properties = Properties()
        properties.load(localPropertiesFile.inputStream())
        return properties.getProperty(propertyName)
    } else {
        println("local.properties file not found")
        return null
    }
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
                "is not set in local.properties"
            }
            buildConfigField("String", "WSS_BASE_URL", wssBaseUrl)

            val directionsKey: String = gradleLocalProperties(rootDir, providers).getProperty("DIRECTIONS_KEY")
            require(wssBaseUrl.isNotEmpty()) {
                "is not set in local.properties"
            }
            buildConfigField("String", "DIRECTIONS_KEY", directionsKey)

            val glApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("ML_API_KEY")
            require(glApiKey.isNotEmpty()) {
                "glApiKey is not set in local.properties"
            }
            buildConfigField("String", "GL_API_KEY", glApiKey)

            val baseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
            require(baseUrl.isNotEmpty()) {
                "glApiKey is not set in local.properties"
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

            val glApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("ML_API_KEY")
            require(glApiKey.isNotEmpty()) {
                "is not set in local.properties"
            }
            buildConfigField("String", "GL_API_KEY", glApiKey)

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
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.location)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.graphics.shapes.android)
    //implementation(libs.compose.preview.renderer)
    //implementation(libs.androidx.navigation.safe.args.generator)
    val room_version = "2.6.1"

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
    implementation("androidx.credentials:credentials:1.3.0-alpha04")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha04")


    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    implementation ("androidx.navigation:navigation-runtime-ktx:2.7.7")

    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.maps.android:maps-compose:4.3.3")


    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)

    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.hilt:hilt-work:1.2.0")


    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
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