package com.example.kotlintasks

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.kotlintasks.navigation_router.navigation.Router
import com.example.kotlintasks.navigation_router.navigation.RouterImpl
import com.example.kotlintasks.navigation_router.navigation.Screen
import com.example.kotlintasks.navigation_router.presentation.SimpleFragment

class KotlinTasksApp : Application() {
    lateinit var router: Router

    override fun onCreate() {
        initRouter()
        createChannel()
        instance = this
        super.onCreate()
    }

    private fun initRouter() {
        router = RouterImpl(listOf(
            Screen("key1") { SimpleFragment.newInstance("First fragment") },
            Screen("key2") { SimpleFragment.newInstance("Second fragment") },
            Screen("key3") { SimpleFragment.newInstance("Third fragment") }
        ))
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        lateinit var instance: KotlinTasksApp
        private const val CHANNEL_NAME = "battery level channel"
        const val CHANNEL_ID = "battery level id"
    }
}