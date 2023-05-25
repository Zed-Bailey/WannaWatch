package com.zed.wannawatch.ui.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valentinilk.shimmer.shimmer
import kotlin.math.min

@Composable
fun AnimatedImageLoader(url: String, width: Dp, height: Dp, contentDescription: String? = null) {

    // animation is used from here
//    https://www.sinasamaki.com/loading-images-using-coil-in-jetpack-compose/

    var loading by remember {
        mutableStateOf(false)
    }

    var success by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf(false)
    }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
            .build(),
        onState = {
            // update mutable values on state changes
            when (it) {
                is AsyncImagePainter.State.Loading -> loading = true
                is AsyncImagePainter.State.Success -> {
                    success = true
                    loading = false
                }
                else -> {
                    error = true
                    loading = false
                }

            }
        },
        contentScale = ContentScale.FillBounds
    )
    val state = painter.state

    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
    )

    if (loading) {

        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .shimmer()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
        }

    } else if (success) {

        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(5.dp))
                .scale(.8f + (.2f * transition))
                .graphicsLayer { rotationX = (1f - transition) * 5f }
                .alpha(min(1f, transition / .2f)),
        )

    } else if (error) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(width)
                .height(height)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))

        ) {
            Icon(Icons.Default.Image, "", tint = Color.White)
        }

    }

}