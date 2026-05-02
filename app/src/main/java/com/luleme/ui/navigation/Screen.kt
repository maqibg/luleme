package com.luleme.ui.navigation

sealed class Screen(val route: String) {
    object LockCheck : Screen("lock_check")
    object Lock : Screen("lock")
    object Home : Screen("home")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
}
