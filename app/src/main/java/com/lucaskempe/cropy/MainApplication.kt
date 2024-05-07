package com.lucaskempe.cropy

import android.app.Application
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()


    override fun onCreate() {
        super.onCreate()
    }

}