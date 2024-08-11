package com.lucaskempe.cropy

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.cropy.workers.delegate.WorkerFactoryDelegate
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application() {

/*
TODO("REPLACING for now since workers depends on multiple repositories")
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()
*/

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerFactoryEntryPoint {
        fun workerFactory(): WorkerFactoryDelegate
    }

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        val workManagerConfiguration: Configuration = Configuration.Builder()
            .setWorkerFactory(EntryPoints.get(this, WorkerFactoryEntryPoint::class.java).workerFactory())
            .build()
        WorkManager.initialize(this, workManagerConfiguration)
        super.onCreate()
    }

}