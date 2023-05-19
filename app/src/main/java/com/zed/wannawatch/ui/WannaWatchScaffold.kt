package com.zed.wannawatch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.zed.wannawatch.ui.navigation.Screen

data class ScaffoldState(
    val shouldShowBack: Boolean = false,
    val onBackPressed: () -> Unit = {},

    /**
     * the items to place on the right of the app bar
     */
    val actions: @Composable() (RowScope.() -> Unit)
)

data class NavButtonItem(
    val icon: ImageVector,
    val name: String,
    val route: Screen
)

// tab bar animation values
const val Duration = 500
const val DoubleDuration = 1000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WannaWatchScaffold(
//    scaffoldState: ScaffoldState,
    navController: NavController,
    content: @Composable () -> Unit
) {

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    val navItems = listOf(
        NavButtonItem(Icons.Rounded.Home, "Home", Screen.DiscoverScreen),
        NavButtonItem(Icons.Rounded.Favorite, "Favourite", Screen.HomeScreen),
        NavButtonItem(Icons.Rounded.Search, "Search", Screen.SearchScreen),
        NavButtonItem(Icons.Rounded.Settings, "Settings", Screen.SettingsScreen),
    )

    val navigationIcon: (@Composable () -> Unit)? =
        if (navController.previousBackStackEntry != null) {
            {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, "")
                }
            }
        } else {
            // this can be null or another component
            // If null, the navigationIcon won't be rendered at all
            null
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("WannaWatch", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                },

                navigationIcon = {
                    if(navController.currentDestination?.route == Screen.DetailScreen.route) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "")
                        }
                    }

                }

            )
        },

    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            content()

        }
    }

}