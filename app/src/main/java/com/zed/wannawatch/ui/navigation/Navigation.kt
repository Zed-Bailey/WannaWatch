package com.zed.wannawatch.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zed.wannawatch.ui.screens.detail.DetailScreen
import com.zed.wannawatch.ui.screens.main.HomeScreen
import com.zed.wannawatch.ui.screens.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        NavigationHost(navController = navController)
    }


}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        startDestination = Screen.HomeScreen.route,
        navController = navController
    ) {


        // MARK: home screen
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }

        // MARK: detail screen
        composable(
            route = Screen.DetailScreen.route + "/{movieId}",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){
            DetailScreen(
                navController = navController,
                // can be forced unwrapped as the argument should never be nullable
                movieId = it.arguments?.getString("movieId")!!
            )
        }

        // MARK: search screen
        composable(
            route = Screen.SearchScreen.route,
        ) {
            SearchScreen(navController)
        }
    }
}