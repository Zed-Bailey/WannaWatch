package com.zed.wannawatch.ui.screens.discoverDetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zed.wannawatch.services.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.utils.TMDBConstants

@Composable
fun SeriesDetailDialog(
    model: TvDetailResult,
    onAdd: () -> Unit,
) {
    val genres = model.genres.map { it.name }

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())

    ) {
        AsyncImage(
            model = if(model.poster_path == null) ""  else TMDBConstants.imageBasePathOriginal + model.poster_path,
            contentDescription = "poster image for ${model.name}",
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .height(170.dp)
                .align(Alignment.CenterHorizontally)
        )


        Text(
            model.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        ) {

            Text(text = model.first_air_date.take(4))

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                if (model.episode_run_time.firstOrNull() == null)
                    "Runtime Unavailable"
                else
                    "Runtime ${model.episode_run_time.first()} mins"
            )

            Spacer(modifier = Modifier.height(2.5.dp))

            Text("${model.number_of_seasons} seasons ${model.number_of_episodes} episodes")

            Spacer(modifier = Modifier.height(2.5.dp))

            LazyRow() {
                items(genres.size) {
                    Text(
                        genres[it],
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                RoundedCornerShape(5.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.5.dp)
                    )
                }
            }
        }

        Text(model.overview, style = MaterialTheme.typography.bodyMedium)

        Button(
            onClick = onAdd,
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(5.dp)
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "I Wanna Watch!")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Seasons")

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Text("number")
                Text("name")
                Text(" episodes")
            }

            Box(modifier = Modifier.fillMaxWidth(0.8f).background(MaterialTheme.colorScheme.onSurface).height(2.dp)) {}

            for(season in model.seasons) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Text("${season.season_number}")
                    Text(season.name)
                    Text("${season.episode_count}")
                }
            }
        }




    }


}