package com.zed.wannawatch.ui.screens.discoverDetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zed.wannawatch.services.models.movie.Movie
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.utils.TMDBConstants
import com.zed.wannawatch.ui.ErrorState
import com.zed.wannawatch.ui.screens.search.MovieDetailDialog

@Composable
fun DiscoverDetailScreen(
    viewModel: DiscoverDetailViewModel = hiltViewModel()
) {

    val loading by viewModel.loading

    val movieDetail by viewModel.movieDetail.observeAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getDetail()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if(loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {

            when(viewModel.type!!) {
                MovieType.Movie -> {
                    MovieDetailDialog(model = viewModel.movieDetail.value!!) {
                        // todo
                        movieDetail?.let {
                            val movie = Movie(
                                imdbID = it.imdb_id!!,
                                tmdbId = it.id,
                                title = it.title,
                                description = it.overview ?: it.tagline ?: "No description provided",
                                year = it.release_date.take(4).toInt(),
                                resultType = MovieType.Movie,
                                posterUrl = if (it.poster_path == null) "" else TMDBConstants.imageBasePath + it.poster_path
                            )

                            viewModel.addModel(movie)

                            Toast.makeText(context, "Added Movie", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                MovieType.Series -> {

                }
            }
        }

        if(viewModel.error.value is ErrorState.Error) {

        }


    }
}