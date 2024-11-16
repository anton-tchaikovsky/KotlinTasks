package com.example.kotlintasks.navigation_router.navigation

interface Navigator {
    fun executeNavigation(currentScreen: Screen?, moveToScreen: Screen){

    }
}