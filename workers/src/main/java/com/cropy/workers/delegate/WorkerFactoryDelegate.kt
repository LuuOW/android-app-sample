package com.cropy.workers.delegate

import androidx.work.DelegatingWorkerFactory
import com.cropy.workers.factory.AppWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerFactoryDelegate @Inject constructor(
    appWorkerFactory: AppWorkerFactory
): DelegatingWorkerFactory() {
    init {
        addFactory(appWorkerFactory)
    }
}