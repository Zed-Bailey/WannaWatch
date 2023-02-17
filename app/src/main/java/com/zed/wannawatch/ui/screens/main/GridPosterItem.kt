package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zed.wannawatch.R
import com.zed.wannawatch.ui.screens.search.AnimatedImageLoader


@Composable
fun GridPosterItem(watched: Boolean, posterUrl: String, onclick: () -> Unit) {

    Box(
        modifier = Modifier
            .width(128.dp)
            .clickable {
                onclick()
            }
    ) {

        AnimatedImageLoader(url = posterUrl, width = 128.dp, height = 150.dp)


        if(watched) {
            Icon(
                painter = painterResource(id = R.drawable.watched_tick),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd),
                tint = Color.Unspecified
            )

        }

    }
}