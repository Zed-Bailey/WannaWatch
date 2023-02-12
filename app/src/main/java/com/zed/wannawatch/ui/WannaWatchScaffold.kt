package com.zed.wannawatch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ScaffoldState(
    val shouldShowBack: Boolean = false,
    val shouldShowDelete: Boolean = false,
    val onBackPressed: () -> Unit = {},
    val onDeletePressed: () -> Unit = {}
)

@Composable
fun WannaWatchScaffold(
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit
) {

    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            if(scaffoldState.shouldShowBack) {
                IconButton(
                    onClick = scaffoldState.onBackPressed,
                ) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("WannaWatch", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            if(scaffoldState.shouldShowDelete) {
                IconButton(
                    onClick = scaffoldState.onDeletePressed,
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = null)
                }
            }

        }
        Spacer(Modifier.height(10.dp))
        content()

    }


}