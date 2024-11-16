package com.example.kotlintasks

import android.app.Application
import com.example.kotlintasks.navigation_router.navigation.Router
import com.example.kotlintasks.navigation_router.navigation.RouterImpl
import com.example.kotlintasks.navigation_router.navigation.Screen
import com.example.kotlintasks.navigation_router.presentation.SimpleFragment

class KotlinTasksApp: Application() {
    lateinit var router: Router

    override fun onCreate() {
        initRouter()
        instance = this
        super.onCreate()
    }

    private fun initRouter(){
        router = RouterImpl(listOf(
            Screen("key1"){ SimpleFragment.newInstance("First fragment")},
            Screen("key2"){ SimpleFragment.newInstance("Second fragment")},
            Screen("key3"){ SimpleFragment.newInstance("Third fragment")}
        ))
    }

    companion object {
        lateinit var instance: KotlinTasksApp
    }
}