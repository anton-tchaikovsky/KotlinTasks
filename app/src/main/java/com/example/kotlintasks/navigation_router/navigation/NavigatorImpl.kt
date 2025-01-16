package com.example.kotlintasks.navigation_router.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class NavigatorImpl(private val fragmentManager: FragmentManager, private val containerId: Int) :
    Navigator {
    override fun executeNavigation(currentScreen: Screen?, moveToScreen: Screen) {
        with(fragmentManager) {
            val moveToFragment: Fragment? = findFragmentByTag(moveToScreen.keyFragment)
            if (currentScreen == null)
                beginTransaction()
                    .add(containerId, moveToScreen.fragmentCreator.invoke(), moveToScreen.keyFragment)
                    .commit()
            else if (moveToFragment == null)
                beginTransaction()
                    .hide(requireNotNull(findFragmentByTag(currentScreen.keyFragment)))
                    .add(containerId, moveToScreen.fragmentCreator.invoke(), moveToScreen.keyFragment)
                    .commit()
            else
                beginTransaction()
                    .hide(requireNotNull(findFragmentByTag(currentScreen.keyFragment)))
                    .show(moveToFragment)
                    .commit()
        }
    }
}