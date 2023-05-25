package com.zed.wannawatch.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.ui.AppBarState
import com.zed.wannawatch.ui.screens.detail.DetailScreen
import com.zed.wannawatch.ui.screens.discover.DiscoverScreen
import com.zed.wannawatch.ui.screens.discoverDetail.DiscoverDetailScreen
import com.zed.wannawatch.ui.screens.saved.SavedScreen
import com.zed.wannawatch.ui.screens.search.SearchScreen
import com.zed.wannawatch.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun Navigation() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    val snackbarHostState = remember { SnackbarHostState() }



    var appBarState by remember {
        mutableStateOf(AppBarState())
    }


    Surface(

        color = MaterialTheme.colorScheme.background
    ) {


        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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

                ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
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
                                snackbarHostState,
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

                        bottomSheet(
                            route = Screen.DiscoverDetailScreen.route + "/{tmdbId}/{type}",
                            arguments = listOf(
                                navArgument("tmdbId") {
                                    type = NavType.IntType
                                    nullable = false
                                },
                                navArgument("type") {
                                    type = NavType.EnumType(type = MovieType::class.java)
                                    nullable = false
                                }
                            )
                        ) {
                            DiscoverDetailScreen()
                        }

                    }
                }

            }
        }
    }
}




@Composable
fun BottomNavBar(navController: NavController) {

    // todo: move to new file
    data class NavButtonItem(val icon: ImageVector, val name: String, val route: Screen)

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
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
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