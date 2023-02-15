package com.zed.wannawatch.ui.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object SearchScreen : Screen("search_screen")
    object DetailScreen: Screen("detail_screen")

    object WatchScreen: Screen("watch_screen")
}
