package com.example.kotlintasks.work_manager

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.BatteryManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.kotlintasks.KotlinTasksApp
import com.example.kotlintasks.R

class ChargingWork(val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    private val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    override suspend fun doWork(): Result {
            val batteryLevel =
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, createNotification(batteryLevel))
            return Result.success()
    }

    private fun createNotification(batteryLevel: Int): Notification {
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        return NotificationCompat.Builder(applicationContext, KotlinTasksApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Charging")
            .setContentText("Battery level $batteryLevel %")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(android.R.drawable.ic_delete, "Cancel", intent)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 123
    }
}