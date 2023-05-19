package com.zed.wannawatch.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.valentinilk.shimmer.shimmer

@Composable
fun DiscoverScreen(navController: NavController) {

    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Trending()
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) { Spacer(modifier = Modifier.height(20.dp)) }


        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            LatestTrailers()
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) { Spacer(modifier = Modifier.height(20.dp)) }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text("What's popular", style = MaterialTheme.typography.headlineSmall)
        }

        for (i in 0..25) {
            item(key = i) {
                Box(
                    modifier = Modifier
                        .height(170.dp)
                        .width(128.dp)
                        .shimmer()
                        .background(Color.Gray, RoundedCornerShape(10))
                ) {}
            }

        }
    }
}

@Composable
private fun LatestTrailers() {
    Column() {
        Text("Latest Trailers", style = MaterialTheme.typography.bodyLarge)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),

        ) {
            item{ Spacer(modifier = Modifier.width(5.dp))}
            for (i in 0..10) {
                item(key = i) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(200.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(128.dp)
                                .shimmer()
                                .background(Color.Gray, RoundedCornerShape(10))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "play",
                                tint = Color.White,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(50.dp)
                            )
                        }
                        Text(
                            "This is the name this is more will it wrap",
                            softWrap = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

            }

        }
    }
}


@Composable
fun Trending() {
    Column() {
        Text("Trending", style = MaterialTheme.typography.headlineSmall)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item{ Spacer(modifier = Modifier.width(5.dp))}

            for (i in 0..10) {
                item(key = i) {
                    Box(
                        modifier = Modifier
                            .height(170.dp)
                            .width(128.dp)
                            .shimmer()
                            .background(Color.Gray, RoundedCornerShape(15))
                    ) {}
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewDiscover() {
    val nav = NavController(LocalContext.current)
    DiscoverScreen(navController = nav)
}
