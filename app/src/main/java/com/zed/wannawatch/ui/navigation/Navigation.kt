package com.zed.wannawatch.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
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

    // navigation icon not recomposing solved with ths help of this answer
    // https://stackoverflow.com/a/68700967
    var canPop by remember { mutableStateOf(false) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
            canPop = controller.previousBackStackEntry != null
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "WannaWatch", fontWeight = FontWeight.Bold)
                },

                navigationIcon = {
                    if (canPop) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                    } else {
                        null
                    }
                },
                actions =  {
                    if(navController.currentDestination?.route == Screen.DetailScreen.route) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Rounded.Delete, contentDescription = null)
                        }
                    }
                }

            )
        },


    ){

        Box(modifier = Modifier.padding(it)) {
            NavigationHost(navController = navController)
        }
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
            HomeScreen(
                movieClicked = {
                    navController.navigate(Screen.DetailScreen.route + "/${it}")
                },
                searchClicked = {
                    navController.navigate(Screen.SearchScreen.route)
                }
            )
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
            DetailScreen(movieId = it.arguments?.getString("movieId")!!)
        }

        // MARK: search screen
        composable(
            route = Screen.SearchScreen.route,
        ) {
            SearchScreen()
        }
    }
}