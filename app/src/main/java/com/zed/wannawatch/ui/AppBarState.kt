package com.zed.wannawatch.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

// state class to allow screen to modify the top app bar
data class AppBarState(
    val navigation: @Composable () -> Unit = {},
    val actions: (@Composable RowScope.() -> Unit)? = null
)