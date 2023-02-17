package com.zed.wannawatch.ui.screens.detail

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zed.wannawatch.R
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.ui.ScaffoldState
import com.zed.wannawatch.ui.WannaWatchScaffold
import com.zed.wannawatch.ui.screens.search.AnimatedImageLoader

@Composable
fun DetailScreen(
    navController: NavController,
    movieId: String,
    viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(
            repository = (LocalContext.current.applicationContext as MovieApplication).repository,
            movieId = movieId
        )
    )
) {

    val movieState by viewModel.movieState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMovie(movieId)
    }

    WannaWatchScaffold(
        scaffoldState = ScaffoldState(
            shouldShowBack = true,
            onBackPressed = {
                navController.navigateUp()
            },
            actions = {
                IconButton(
                    onClick = {
                        Log.i("com.zed.wannawatch", "delete pressed")
                        viewModel.delete(movieState)
                        navController.popBackStack()
                    }
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = null)
                }
            }

        )
    ) {


        movieState?.let {movie ->
            Details(
                movie = movie,
                viewModel = viewModel,
                onWatchClicked = {
                    navController.navigate("watch_screen/${movie.imdbID}")
                }
            )
        }






    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    movie: Movie,
    viewModel: DetailViewModel,
    onWatchClicked : () -> Unit
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

        FilledTonalButton(
            shape = RoundedCornerShape(10.dp),
            onClick = {
                onWatchClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp, 5.dp)
                .align(CenterHorizontally),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)

        ) {
            Text(text = "Watch Online", color = MaterialTheme.colorScheme.onSecondary)
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
