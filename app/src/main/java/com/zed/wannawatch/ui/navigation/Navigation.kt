package com.zed.wannawatch.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zed.wannawatch.ui.AppBarState
import com.zed.wannawatch.ui.NavButtonItem
import com.zed.wannawatch.ui.screens.detail.DetailScreen
import com.zed.wannawatch.ui.screens.discover.DiscoverScreen
import com.zed.wannawatch.ui.screens.saved.SavedScreen
import com.zed.wannawatch.ui.screens.search.SearchScreen
import com.zed.wannawatch.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    var appBarState by remember {
        mutableStateOf(AppBarState())
    }


    Surface(
        color = MaterialTheme.colorScheme.background
    ) {


        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("WannaWatch", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    },
                    navigationIcon = appBarState.navigation,
                    actions = {
                        appBarState.actions?.invoke(this)
                    }

                )
            },
            bottomBar = {
                BottomNavBar(navController = navController)
            }

            ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)
            ) {
                
                NavHost(
                    startDestination = Screen.DiscoverScreen.route,
                    navController = navController
                ) {


                    // MARK: home screen
                    composable(route = Screen.HomeScreen.route) {
                        SavedScreen(navController)
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
//                            movieId = it.arguments?.getString("movieId")!!,
                            onComposing = { state ->
                                appBarState = state
                            }
                        )
                    }

                    // MARK: search screen
                    composable(
                        route = Screen.SearchScreen.route ,
                    ) {
                        SearchScreen(navController)
                    }

                    composable(
                        route = Screen.DiscoverScreen.route
                    ) {
                        DiscoverScreen(navController = navController)
                    }

                    composable(
                        route = Screen.SettingsScreen.route
                    ) {
                        SettingsScreen(navController = navController)
                    }
                }
            }
        }
    }
}


@Composable
fun BottomNavBar(navController: NavController) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }

    val navItems = listOf(
        NavButtonItem(Icons.Rounded.Home, "Home", Screen.DiscoverScreen),
        NavButtonItem(Icons.Rounded.Bookmark, "Favourite", Screen.HomeScreen),
        NavButtonItem(Icons.Rounded.Search, "Search", Screen.SearchScreen),
        NavButtonItem(Icons.Rounded.Settings, "Settings", Screen.SettingsScreen),
    )


    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        navItems.forEachIndexed { index, navButtonItem ->

            IconButton(onClick = {
                if(index != selectedIndex) {
                    selectedIndex = index
                    navController.navigate(navButtonItem.route.route)
                }
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(navButtonItem.icon, "", tint = if(selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                    if(selectedIndex == index) {
                        Box(modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)){}
                    }

                }
            }

        }

    }
}


@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = NavController(LocalContext.current))
}