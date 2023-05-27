package com.zed.wannawatch.ui.screens.discoverDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.zed.wannawatch.services.models.movie.Movie
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.utils.TMDBConstants
import com.zed.wannawatch.ui.ErrorState
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun DiscoverDetailScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: DiscoverDetailViewModel = hiltViewModel()
) {

    val loading by viewModel.loading

    val movieDetail by viewModel.movieDetail.observeAsState()
    val seriesDetail by viewModel.seriesDetail.observeAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.getDetail()
    }


    val errorState by viewModel.error
    LaunchedEffect(errorState) {
        when (val e = errorState) {
            is ErrorState.Error -> {
                snackbarHostState.showSnackbar(e.msg)
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
            ),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {

            when (viewModel.type!!) {
                MovieType.Movie -> {
                    movieDetail?.let {
                        MovieDetailDialog(model = it) {

                            val movie = Movie(
                                imdbID = it.imdb_id!!,
                                tmdbId = it.id,
                                title = it.title,
                                description = it.overview ?: it.tagline
                                ?: "No description provided",
                                year = it.release_date.take(4).toInt(),
                                resultType = MovieType.Movie,
                                posterUrl = if (it.poster_path == null) "" else TMDBConstants.imageBasePath + it.poster_path
                            )

                            viewModel.addModel(movie)

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Added Movie", withDismissAction = true)
                            }

                        }

                    }
                }


                MovieType.Series -> {
                    seriesDetail?.let {
                        SeriesDetailDialog(
                            model = it,
                            onAdd = {
                                val movie = Movie(
                                    imdbID = "",
                                    tmdbId = it.id,
                                    title = it.name,
                                    description = it.overview,
                                    year = it.first_air_date.take(4).toInt(),
                                    resultType = MovieType.Series,
                                    posterUrl = TMDBConstants.imageBasePath + (it.poster_path
                                        ?: it.backdrop_path),
                                    seriesSeasons = Json.encodeToString(value = it.seasons),
                                )
                                viewModel.addModel(movie)

                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Added Series", withDismissAction = true)
                                }

                            },
                        )
                    }
                }
            }
        }
    }
}

