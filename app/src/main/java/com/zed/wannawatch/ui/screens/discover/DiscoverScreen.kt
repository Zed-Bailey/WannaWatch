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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.valentinilk.shimmer.shimmer
import com.zed.wannawatch.services.models.tmdb.discover.DiscoverMovies
import com.zed.wannawatch.services.utils.TMDBConstants

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel(),
    navController: NavController
) {

    val data by viewModel.data
    val loading by viewModel.loading
    val errorState by viewModel.errorState


    LaunchedEffect(Unit) {
        viewModel.load()
    }


    if(loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else {
        when (val state = errorState) {
            is DiscoverViewModel.ErrorState.NoError -> {
                data?.let {
                    DiscoverView(it)
                }
            }
            is DiscoverViewModel.ErrorState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.msg)
                        Button(onClick = { viewModel.load() }) {
                            Text("retry")
                        }
                    }
                }
            }
        }

    }


}
//{
//    "adult": false,
//    "backdrop_path": "/nLBRD7UPR6GjmWQp6ASAfCTaWKX.jpg",
//    "genre_ids": [
//    16,
//    10751,
//    12,
//    14,
//    35
//    ],
//    "id": 502356,
//    "original_language": "en",
//    "original_title": "The Super Mario Bros. Movie",
//    "overview": "While working underground to fix a water main, Brooklyn plumbers—and brothers—Mario and Luigi are transported down a mysterious pipe and wander into a magical new world. But when the brothers are separated, Mario embarks on an epic quest to find Luigi.",
//    "popularity": 8501.774,
//    "poster_path": "/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
//    "release_date": "2023-04-05",
//    "title": "The Super Mario Bros. Movie",
//    "video": false,
//    "vote_average": 7.6,
//    "vote_count": 2570
//},


@Composable
fun DiscoverView(data: DiscoverMovies) {
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

            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = TMDBConstants.backdropBasePath + data.results.first().backdrop_path,
                    contentDescription = "backdrop path",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.Center
                )

                Text(
                    text = data.results.first().title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    softWrap = true,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                0F to Color.Transparent,
                                .5F to Color.Black.copy(alpha = 0.5F),
                                1F to Color.Black.copy(alpha = 0.8F)
                            )
                        )
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 24.dp),
                    color = Color.White
                )
            }
        }

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
