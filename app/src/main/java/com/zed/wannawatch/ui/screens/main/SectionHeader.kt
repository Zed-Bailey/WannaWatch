package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(text: String) {
    Column() {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 3.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
    }

}