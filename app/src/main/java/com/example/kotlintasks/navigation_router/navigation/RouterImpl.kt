package com.example.kotlintasks.navigation_router.navigation

class RouterImpl(private val screens: List<Screen>) : Router {

    private var navigator: Navigator? = null

    private var currentScreenIndex = 0

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun removeNavigator() {
        this.navigator = null
    }

    override fun start() {
        currentScreenIndex = 0
        navigator?.executeNavigation(
            null,
            screens[currentScreenIndex]
        )
    }

    override fun back() {
        navigator?.executeNavigation(
            screens[currentScreenIndex],
            screens[getBackScreenIndex().also { currentScreenIndex = it }]
        )
    }

    override fun next() {
        navigator?.executeNavigation(
            screens[currentScreenIndex],
            screens[getNextScreenIndex().also { currentScreenIndex = it }]
        )
    }

    private fun getNextScreenIndex(): Int =
        if (currentScreenIndex == screens.size - 1)
            0
        else
            currentScreenIndex + 1

    private fun getBackScreenIndex(): Int =
        if (currentScreenIndex == 0)
            screens.size - 1
        else currentScreenIndex - 1
}