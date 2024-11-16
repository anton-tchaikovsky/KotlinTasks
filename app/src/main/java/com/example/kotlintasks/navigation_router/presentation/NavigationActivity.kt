package com.example.kotlintasks.navigation_router.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintasks.KotlinTasksApp
import com.example.kotlintasks.R
import com.example.kotlintasks.navigation_router.navigation.Navigator
import com.example.kotlintasks.navigation_router.navigation.NavigatorImpl
import com.example.kotlintasks.navigation_router.navigation.Router

class NavigationActivity : AppCompatActivity(), NavigationRequestListener {

    private val router: Router = KotlinTasksApp.instance.router

    private val navigator: Navigator =
        NavigatorImpl(supportFragmentManager, R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navigation)
        router.setNavigator(navigator)
        if (savedInstanceState == null)
            router.start()
    }

    override fun onBack() {
        router.back()
    }

    override fun onNext() {
        router.next()
    }

    override fun onDestroy() {
        router.removeNavigator()
        super.onDestroy()
    }
}