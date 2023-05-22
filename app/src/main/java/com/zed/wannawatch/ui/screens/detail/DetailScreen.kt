package com.zed.wannawatch.ui.screens.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zed.wannawatch.R
import com.zed.wannawatch.services.models.movie.Movie
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.utils.TMDBConstants
import com.zed.wannawatch.ui.AppBarState
import com.zed.wannawatch.ui.screens.BackDropImage
import com.zed.wannawatch.ui.screens.search.AnimatedImageLoader

@Composable
fun DetailScreen(
    navController: NavController,
    onComposing: (AppBarState) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val detailState by viewModel.movieState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMovie()
    }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                actions = {
                    IconButton(onClick = {
                        viewModel.delete(detailState)
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                },
                navigation = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        )
    }

    detailState?.let {movie ->
        when(movie.resultType) {
            MovieType.Movie -> {
                MovieDetailScreen(movie = movie, viewModel = viewModel)
            }

            MovieType.Series -> {
                TvDetailScreen(movie = movie, viewModel = viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvDetailScreen(
    movie: Movie,
    viewModel: DetailViewModel
) {

    val tvDetail by viewModel.tvDetailResult
    val loading by viewModel.loading

    LaunchedEffect(Unit) {
        viewModel.getTvDetails()
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            CircularProgressIndicator()
        }
    } else if (tvDetail == null) {

        BasicDetailView(movie, viewModel)

    } else {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


            BackDropImage(
                url = TMDBConstants.backdropBasePath + tvDetail?.backdrop_path,
                title = movie.title
            ) {

                Button(
                    modifier = Modifier.padding(end = 5.dp),
                    onClick = { viewModel.toggleWatched() }) {
                    Text(
                        text = if (movie.watched) "UnWatch" else "Watched",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp, 15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    AnimatedImageLoader(url = movie.posterUrl, width = 128.dp, height = 170.dp)

                    Spacer(modifier = Modifier.width(15.dp))

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${tvDetail?.seasons?.size} seasons",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "${tvDetail?.genresString}",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "${tvDetail?.first_air_date}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }


                Text(tvDetail?.overview ?: "")

                Spacer(modifier = Modifier.height(25.dp))

                RatingRow(
                    currRating = movie.rating,
                    onClick = { ratingValue ->
                        viewModel.updateRating(ratingValue)
                    }
                )


                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    "Series Notes",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp)
                )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .padding(15.dp),
                    value = movie.notes,
                    onValueChange = {
                        viewModel.updateNotesText(it)
                    },
                    maxLines = 10,
                    placeholder = {
                        Text(text = "Write any notes here")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: Movie,
    viewModel: DetailViewModel
) {

    val movieDetail by viewModel.movieDetailResult
    val loading by viewModel.loading

    LaunchedEffect(Unit) {
        viewModel.getMovieDetails()
    }

    if(loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            CircularProgressIndicator()
        }
    } else if(movieDetail == null) {

        BasicDetailView(movie, viewModel)

    } else {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


            BackDropImage(url = TMDBConstants.backdropBasePath + movieDetail?.backdrop_path, title = movie.title) {

                Button(
                    modifier = Modifier.padding(end = 5.dp),
                    onClick = { viewModel.toggleWatched() }) {
                    Text(
                        text = if (movie.watched) "UnWatch" else "Watched",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp, 15.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AnimatedImageLoader(url = movie.posterUrl, width = 128.dp, height = 170.dp)

                        Spacer(modifier = Modifier.width(15.dp))

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${movieDetail?.runtime}", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${movieDetail?.genres?.forEach { it.name + ' ' }}",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                "${movieDetail?.release_date}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }


                    Text(movieDetail?.overview ?: "")


                    RatingRow(
                        currRating = movie.rating,
                        onClick = { ratingValue ->
                            viewModel.updateRating(ratingValue)
                        }
                    )


                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        "Movie Notes",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp)
                    )

                    OutlinedTextField(
                        colors = TextFieldDefaults.outlinedTextFieldColors(MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(128.dp)
                            .padding(15.dp),
                        value = movie.notes,
                        onValueChange = {
                            viewModel.updateNotesText(it)
                        },
                        maxLines = 10,
                        placeholder = {
                            Text(text = "Write any notes here")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDetailView(
    movie: Movie,
    viewModel: DetailViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            AnimatedImageLoader(url = movie.posterUrl, width = 200.dp, height = 300.dp)
        }


        Spacer(modifier = Modifier.height(10.dp))

        Text(
            movie.title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold
        )

        Button(
            shape = RoundedCornerShape(10.dp),
            onClick = {
                viewModel.toggleWatched()
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp, 10.dp)
                .align(CenterHorizontally),

        ) {
            Text(
                text = if (movie.watched) "Un-Watch" else "Watched",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(25.dp))

        RatingRow(
            currRating = movie.rating,
            onClick = { ratingValue ->
                viewModel.updateRating(ratingValue)
            }
        )


        Spacer(modifier = Modifier.height(25.dp))

        Text(
            "Movie Notes",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 20.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp)
        )

        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(15.dp),
            value = movie.notes,
            onValueChange = {
                viewModel.updateNotesText(it)
            },
            maxLines = 10,
            placeholder = {
                Text(text = "Write any notes here")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        }

}


@Composable
fun RatingRow(currRating: Int, onClick: (Int) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .horizontalScroll(
                rememberScrollState()
            )
    ) {
        RatingIcon(
            iconId = R.drawable.vomiting_face,
            onClick = { onClick(1) },
            selected = currRating == 1
        )
        RatingIcon(
            iconId = R.drawable.frowning_face,
            onClick = { onClick(2) },
            selected = currRating == 2
        )
        RatingIcon(
            iconId = R.drawable.meh_face,
            onClick = { onClick(3) },
            selected = currRating == 3
        )
        RatingIcon(
            iconId = R.drawable.grinning_face_with_big_eyes,
            onClick = { onClick(4) },
            selected = currRating == 4
        )
        RatingIcon(
            iconId = R.drawable.star_struck,
            onClick = { onClick(5) },
            selected = currRating == 5
        )
    }
}

@Composable
fun RatingIcon(iconId: Int, selected: Boolean, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = iconId),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .alpha(if (selected) 1f else 0.5f)
            .clickable(enabled = !selected, onClick = onClick)
    )
}
