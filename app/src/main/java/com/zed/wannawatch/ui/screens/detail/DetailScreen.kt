package com.zed.wannawatch.ui.screens.detail

import android.util.Log
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.zed.wannawatch.R
import com.zed.wannawatch.services.models.Movie
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zed.wannawatch.services.MovieApplication


@Composable
fun DetailScreen(
    movieModel : Movie,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory((LocalContext.current.applicationContext as MovieApplication).repository, movieModel))
) {

    val movieState by viewModel.movieState.collectAsState()

    Details(
        movie = movieState,
        watchedToggle = { viewModel.toggleWatched() },
        ratingOnClick = { viewModel.updateRating(it) },
        onNotesChanged = { viewModel.updateNotesText(it) }
    )



}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(movie: Movie, watchedToggle: () -> Unit, ratingOnClick: (Int) -> Unit, onNotesChanged: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(model = movie.posterUrl, contentDescription = null, modifier = Modifier
            .align(CenterHorizontally)
            .padding(20.dp))

        Text(movie.title, fontSize = 20.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Button(
            shape = RoundedCornerShape(10.dp),
            onClick = {
                watchedToggle()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp, 15.dp)
                .align(CenterHorizontally)

        ) {
            Text(text = if(movie.watched) "unwatch" else "watched")
        }

        RatingRow(
            currRating = movie.rating,
            onClick = { ratingValue ->
                Log.i("com.zed.wannawatch", "rating onclick: $ratingValue")
                ratingOnClick(ratingValue)
            })

        Text("Movie Notes",
            fontSize = 20.sp ,
            textAlign = TextAlign.Left ,
            modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp)
        )

        OutlinedTextField(
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
    }
}


@Composable
fun RatingRow(currRating: Int, onClick: (Int) -> Unit) {

    val options = listOf(("" to ""))

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 20.dp)
        .horizontalScroll(rememberScrollState()
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

@Preview
@Composable
fun DetailsPreview() {
    Surface(modifier = Modifier.background(Color.White)) {
        Details(
            movie = Movie("", "Movie Title", "https://via.placeholder.com/150", rating = 2, watched = true, notes = "this is the notes"),
            watchedToggle = {  },
            ratingOnClick = { },
            onNotesChanged = { }
        )
    }

}

