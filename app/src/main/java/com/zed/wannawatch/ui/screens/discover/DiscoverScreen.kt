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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valentinilk.shimmer.shimmer
import com.zed.wannawatch.services.models.tmdb.trending.movie.TrendingMovieItem
import com.zed.wannawatch.services.models.tmdb.trending.movie.TrendingMovies
import com.zed.wannawatch.services.models.tmdb.trending.tv.TrendingTvShows
import com.zed.wannawatch.services.utils.TMDBConstants
import com.zed.wannawatch.ui.ErrorState
import com.zed.wannawatch.ui.screens.BackDropImage
import com.zed.wannawatch.ui.screens.search.AnimatedImageLoader

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel(),
    navController: NavController
) {

    val movieData by viewModel.movieData
    val tvData by viewModel.tvShowData
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
            is ErrorState.NoError -> {
                DiscoverView(movies = movieData!!, tvShows = tvData!!)
            }

            is ErrorState.Error -> {
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

@Composable
fun DiscoverView(movies: TrendingMovies, tvShows: TrendingTvShows) {
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp)
    ) {

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            HeaderMovieItem(movies.results.first())
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Trending(movies, tvShows)
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
fun HeaderMovieItem(movie: TrendingMovieItem) {

    BackDropImage(url = TMDBConstants.backdropBasePath + movie.backdrop_path, title = movie.title) {
        Button(
            modifier = Modifier.padding(end = 5.dp),
            onClick = { /*TODO add */ }) {
            Text("Add")
        }

        OutlinedButton(onClick = { /*TODO details */ }) {
            Text("Details")
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
fun Trending(movies: TrendingMovies, tvShows: TrendingTvShows) {
    Column() {
        Text("Trending Movies", style = MaterialTheme.typography.headlineSmall)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item{ Spacer(modifier = Modifier.width(5.dp))}

            for (movie in movies.results) {
                item(key = movie.id) {
                    Box(
                        modifier = Modifier
                            .height(170.dp)
                            .width(128.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center

                    ) {
                        AnimatedImageLoader(
                            url = TMDBConstants.imageBasePath + movie.poster_path,
                            width = 128.dp,
                            height = 170.dp
                        )
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Text("Trending Tv Shows", style = MaterialTheme.typography.headlineSmall)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {

            for (show in tvShows.results) {
                item(key = show.id) {
                    Box(
                        modifier = Modifier
                            .height(170.dp)
                            .width(128.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center

                    ) {
                        AnimatedImageLoader(
                            url = TMDBConstants.imageBasePath + show.poster_path,
                            width = 128.dp,
                            height = 170.dp
                        )
                    }
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
