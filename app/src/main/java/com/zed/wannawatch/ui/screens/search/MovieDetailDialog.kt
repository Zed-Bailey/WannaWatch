package com.zed.wannawatch.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zed.wannawatch.services.api.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.repository.TMDBConstants

@Composable
fun MovieDetailDialog(model: MovieDetailResult, detailLoading: Boolean?, onAdd: () -> Unit, onClose: () -> Unit) {
    val genres = model.genres.map { it.name }

    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Rounded.Close, null)
            }
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())

        ) {

            if (detailLoading == true) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
            else {

                AsyncImage(
                    model = TMDBConstants.imageBasePath + (model.poster_path ?: model.backdrop_path),
                    contentDescription = "poster image for ${model.title}",
                    modifier = Modifier
                        .aspectRatio(2f / 3f)
                        .height(170.dp)
                        .align(Alignment.CenterHorizontally)
                )


                Text(
                    model.title,
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

                    Text(text = model.release_date.take(4))

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(

                        if (model.runtime?.toString() != "")
                            "${model.runtime?.toString()} mins runtime"
                        else
                            "Runtime unavailable"
                    )


                    Spacer(modifier = Modifier.height(5.dp))

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

                Text(model.overview ?: model.tagline ?: "No description available", style = MaterialTheme.typography.bodyMedium)

                Button(
                    onClick = onAdd,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "I Wanna Watch!")
                }
            }


        }


    }
}