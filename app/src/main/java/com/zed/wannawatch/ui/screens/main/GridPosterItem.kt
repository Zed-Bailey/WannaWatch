package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zed.wannawatch.R


@Composable
fun GridPosterItem(watched: Boolean, posterUrl: String, onclick: () -> Unit) {

    Box(
        modifier = Modifier
            .width(128.dp)
            .clickable {
                onclick()
            }
    ) {

        AsyncImage(model = posterUrl,
            contentDescription = "movie poster image",
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .width(128.dp)
        )

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