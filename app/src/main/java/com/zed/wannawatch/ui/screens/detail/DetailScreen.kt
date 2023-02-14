package com.zed.wannawatch.ui.screens.detail

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zed.wannawatch.R
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.ui.ScaffoldState
import com.zed.wannawatch.ui.WannaWatchScaffold

@Composable
fun DetailScreen(
    navController: NavController,
    movieId: String,
    viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(
            (LocalContext.current.applicationContext as MovieApplication).repository,
            movieId
        )
    )
) {

    val movieState by viewModel.movieState.collectAsState()


    WannaWatchScaffold(
        scaffoldState = ScaffoldState(
            shouldShowBack = true,
            shouldShowDelete = true,
            onBackPressed = {
                navController.navigateUp()
            },
            onDeletePressed = {
                Log.i("com.zed.wannawatch", "delete pressed")
                viewModel.delete(movieState)
                navController.popBackStack()
            }
        )
    ) {
        if (movieState != null) {
            Details(
                movie = movieState!!,
                watchedToggle = { viewModel.toggleWatched() },
                ratingOnClick = { viewModel.updateRating(it) },
                onNotesChanged = { viewModel.updateNotesText(it) },
            )
        } else {
            Text("Nothing here")
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    movie: Movie,
    watchedToggle: () -> Unit,
    ratingOnClick: (Int) -> Unit,
    onNotesChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = movie.posterUrl, contentDescription = null,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(horizontal = 20.dp)
                .width(200.dp)
                .aspectRatio(2f / 3f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            movie.title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            shape = RoundedCornerShape(10.dp),
            onClick = {
                watchedToggle()
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp, 15.dp)
                .align(CenterHorizontally),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)


        ) {
            Text(
                text = if (movie.watched) "Un-Watch" else "Watched",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Button(
            shape = RoundedCornerShape(10.dp),
            onClick = {
                // todo navigate to stream screen
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp, 15.dp)
                .align(CenterHorizontally),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)

        ) {
            Text(text = "Watch Online", color = MaterialTheme.colorScheme.onSecondary)
        }

        RatingRow(
            currRating = movie.rating,
            onClick = { ratingValue ->
                ratingOnClick(ratingValue)
            }
        )

        Card(
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.padding(15.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Movie Notes",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            )

            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(15.dp),
                value = movie.notes,
                onValueChange = { onNotesChanged(it) },
                maxLines = 10,
                placeholder = {
                    Text(text = "Write any notes here")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
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

//@Preview
//@Composable
//fun DetailsPreview() {
//    Surface(modifier = Modifier.background(Color.White)) {
//        Details(
//            movie = Movie(
//                "",
//                "Movie Title",
//                "https://via.placeholder.com/150",
//                rating = 2,
//                watched = true,
//                notes = "this is the notes",
//                resultType = MovieType.Movie,
//                description = "this is the movie/series description"
//            ),
//            watchedToggle = { },
//            ratingOnClick = { },
//            onNotesChanged = { },
//        )
//    }
//
//}

