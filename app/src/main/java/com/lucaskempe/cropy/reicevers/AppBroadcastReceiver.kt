package com.lucaskempe.cropy.reicevers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.lucaskempe.cropy.R


class AppBroadcastReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = NotificationCompat.Builder(context, "CHANNEL_ID")
                    .setContentTitle("Wifi off")
                    .setContentText("The wifi connection was turned off.")
                    .setSmallIcon(R.drawable.ic_home)
                    .build()
                notificationManager.notify(1, notification)
            }
        }
    }


}