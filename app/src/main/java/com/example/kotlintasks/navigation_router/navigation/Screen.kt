package com.example.kotlintasks.navigation_router.navigation

import androidx.fragment.app.Fragment

class Screen(val keyFragment: String, val fragmentCreator:() -> Fragment)