package com.lucaskempe.cropy.services

import  android.content.Intent
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppService() : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val workManager = WorkManager.getInstance(applicationContext)

        CoroutineScope(Dispatchers.IO+ Job()).launch {
/*            viewModel.selectedImageUri.collect {
                workManager.enqueueUniqueWork("one_time",
                    ExistingWorkPolicy.KEEP,
                    OneTimeWorkRequest.Builder(AppWorker::class.java)
                        .setInputData(workDataOf("imageUri" to it))
                        .build()
                    )
            }*/
        }


/*        val imageUri = intent?.getStringExtra("imageUri")
        workManager.enqueueUniqueWork(
            "one_time_work",
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequest.Builder(AppWorker::class.java)
                .setInputData( workDataOf("imageUri" to imageUri))
                .build()
        )*/
        return super.onStartCommand(intent, flags, startId)
    }

    private fun launchOneTimeWork() {

    }
}
