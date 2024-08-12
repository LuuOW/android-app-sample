package com.lucaskempe.cropy

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.cropy.datastore.user.UserSessionDataStore
import com.cropy.workers.UIWorker
import com.lucaskempe.cropy.reicevers.WebSocketReceiver
import com.lucaskempe.cropy.reicevers.LocationReceiver
import com.lucaskempe.cropy.ui.authentication.AuthViewmodel
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import com.lucaskempe.cropy.viewmodels.MapsViewModel
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<UsersViewModel>()
    private val authViewModel by viewModels<AuthViewmodel>()
    private val mapsViewModel by viewModels<MapsViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()

    private var session by mutableStateOf(false)

    private val locationWorkReq = OneTimeWorkRequestBuilder<com.cropy.workers.LocationWorker>()
        .addTag("LOCATION_WORKER_TAG")
        .build()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(LocationReceiver(mapsViewModel), IntentFilter("ACTION_LOCATION_UPDATE"), RECEIVER_NOT_EXPORTED)

        val wssActions = IntentFilter().apply {
            addAction("ACTION_MESSAGE_RECEIVED")
            addAction("ACTION_BASE64_MESSAGE_RECEIVED")
            addAction("ERROR")
        }
        registerReceiver(WebSocketReceiver(chatViewModel = chatViewModel), wssActions , RECEIVER_NOT_EXPORTED)

        CoroutineScope(Dispatchers.IO).launch {
            applicationContext.UserSessionDataStore.data.collect {
                if (it.token.isNotEmpty()) {
                    session = true
                } else {
                    session = false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (userHasLocationPermission(this)) {
            viewModel.workManager.enqueueUniqueWork("LOCATION_WORKER_TAG", ExistingWorkPolicy.REPLACE, locationWorkReq)
        }

        val workReq = OneTimeWorkRequestBuilder<UIWorker>()
            .addTag("UI_WORKER")
            .setInputData(workDataOf("id" to "1"))
            .build()
        authViewModel.workManager.enqueueUniqueWork("UI_WORKER", ExistingWorkPolicy.REPLACE, workReq)


        installSplashScreen()
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),)
        setContent { MainApp(session, viewModel = viewModel, authViewmodel = authViewModel, mapsViewModel = mapsViewModel, chatViewModel = chatViewModel) }
    }

    override fun onStop() {
        super.onStop()
        viewModel.workManager.cancelAllWorkByTag("LOCATION_WORKER_TAG")
    }

}