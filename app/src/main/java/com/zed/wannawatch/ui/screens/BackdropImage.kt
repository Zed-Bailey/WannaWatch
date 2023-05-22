package com.zed.wannawatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

/**
 * a backdrop image composable.
 * Displays the image with a linear gradient along the bottom to blend the background into the image.
 * @param url the url image path
 * @param title the title to display
 * @param backdropAction placed under the title. add actions or more content here
 */
@Composable
fun BackDropImage(url:String, title: String, backdropAction: (@Composable RowScope.() -> Unit)) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {

        SubcomposeAsyncImage(
            model = url,
            contentDescription = "$title backdrop image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            alignment = Alignment.Center,
            error = {
                Icon(Icons.Default.BrokenImage,  "", tint = Color.White)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        0F to Color.Transparent,
                        .5F to MaterialTheme.colorScheme.background.copy(alpha = 0.5F),
                        1F to MaterialTheme.colorScheme.background.copy(alpha = 0.8F)
                    )
                )
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 24.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                softWrap = true,
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.White
            )

            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                backdropAction.invoke(this)

            }
        }


    }
}