package com.example.kotlintasks.navigation_router.navigation

interface Router {
    fun setNavigator(navigator: Navigator)

    fun removeNavigator()

    fun start()

    fun back()

    fun next()
}