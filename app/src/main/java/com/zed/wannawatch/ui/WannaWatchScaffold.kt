package com.zed.wannawatch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ScaffoldState(
    val shouldShowBack: Boolean = false,
    val onBackPressed: () -> Unit = {},

    /**
     * the items to place on the right of the app bar
     */
    val actions: @Composable() (RowScope.() -> Unit)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WannaWatchScaffold(
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit
) {

    Column() {
        CenterAlignedTopAppBar(
            title = {
                Text("WannaWatch", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            },
            navigationIcon = {
                if(scaffoldState.shouldShowBack) {
                    IconButton(
                        onClick = scaffoldState.onBackPressed,
                    ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                }
            },

            actions = scaffoldState.actions

        )

        Spacer(Modifier.height(10.dp))

        content()

    }


}