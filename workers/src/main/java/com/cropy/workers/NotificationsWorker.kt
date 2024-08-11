package com.cropy.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams){

    val size = inputData.getInt("size", 0)
    private val notificationsManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        createNotificationChannel(context)
        val foregroundInfo = createForegroundInfo()
        setForegroundAsync(foregroundInfo)
        notificationsManager.notify("APP_NOTIFICATION", foregroundInfo.notificationId, foregroundInfo.notification)
        return Result.success(workDataOf("NOTIFICATION_WORKER" to "USER_NOTIFIED"))
    }

    private fun createForegroundInfo(): ForegroundInfo {
/*        val intent = Intent(applicationContext, MainActivity::class.java)
            .apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }*/

       // val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 100, 350, 25, 25, 25, 25, 200)

        val notification = NotificationCompat.Builder(applicationContext, "NOTIFICATION_01")
            .setContentTitle(if (size > 1) "This is a test notification" else "This is a test for multiple notifications")
            .setContentText("In this example if you tap the notification it should open the main activity")
            .setVibrate(timings)
            .setNumber(size)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        return  ForegroundInfo(1, notification)
    }

}

private fun createNotificationChannel(context: Context) {
    //val audioSource = Uri.parse("android.resource://"+context.packageName+"/"+R.raw.effect)
    val audioAttributes = AudioAttributes
        .Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    val name = "APP_NOTIFICATION_01"
    val descriptionText = "app_notification"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("NOTIFICATION_01", name, importance).apply {
        setShowBadge(true)
       // setSound(audioSource, audioAttributes)
        description = descriptionText
    }
    // Register the channel with the system.
    val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

}