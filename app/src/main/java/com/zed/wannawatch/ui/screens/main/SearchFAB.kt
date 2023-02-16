package com.zed.wannawatch.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchFAB(modifier: Modifier, expanded: Boolean, onclick : () -> Unit) {

    ExtendedFloatingActionButton(
        modifier = modifier,
        text = {
            Text(text = "Search")
        },
        icon = {
            Icon(Icons.Rounded.Search, contentDescription = "Search FAB")
        },
        onClick = onclick,
        expanded = expanded,
    )

}