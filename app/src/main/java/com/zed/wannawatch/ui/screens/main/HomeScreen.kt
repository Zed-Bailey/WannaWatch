package com.zed.wannawatch.ui.screens.main

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zed.wannawatch.R
import com.zed.wannawatch.services.HomeScreenWatchedFilter
import com.zed.wannawatch.services.MovieRatingFilter


@Composable
fun HomeScreen(viewModel: MainViewModel) {

    val movies by viewModel.movies.observeAsState()

    var watchedFilterStatus by remember { mutableStateOf(HomeScreenWatchedFilter.All) }
    val watchedFilterOptions = listOf<HomeScreenWatchedFilter>(HomeScreenWatchedFilter.All, HomeScreenWatchedFilter.Watched, HomeScreenWatchedFilter.Unwatched)
    var watchedFilterExpanded by remember { mutableStateOf(false) }

    var ratingFilterValue by remember { mutableStateOf(MovieRatingFilter.All) }
    val ratingFilterOptions = listOf<MovieRatingFilter>(MovieRatingFilter.All, MovieRatingFilter.One, MovieRatingFilter.Two, MovieRatingFilter.Three, MovieRatingFilter.Four, MovieRatingFilter.Five)
    var ratingFilterExpanded by remember { mutableStateOf(false) }


    Box () {
        Column(modifier = Modifier.fillMaxSize()) {
            // filter options
            // todo: export to component
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                // filter movies by watched/unwatched status
                Box(){
                    Button(
                        border = BorderStroke(1.5.dp, Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                        onClick = {
                            watchedFilterExpanded = true
                        }
                    ) {
                        Text(text = "filter watched")
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "drop down arrow")
                    }

                    DropdownMenu(
                        expanded = watchedFilterExpanded,
                        onDismissRequest = { watchedFilterExpanded = false }) {
                        watchedFilterOptions.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    watchedFilterStatus = it
                                    watchedFilterExpanded = false
                                },
                                enabled = (it != watchedFilterStatus),
                                text = {
                                    Row(horizontalArrangement = SpaceBetween) {
                                        Text(it.toString())
                                        if(it == watchedFilterStatus)
                                            Icon(Icons.Rounded.Check, contentDescription = "check mark for filter selected status")
                                    }
                                })
                        }
                    }
                }

                // filter movies by rating
                Box(){
                    Button(
                        border = BorderStroke(1.5.dp, Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                        onClick = {
                            ratingFilterExpanded = true
                        }
                    ) {
                        Text(text = "filter by rating")
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "drop down arrow")
                    }

                    DropdownMenu(
                        expanded = ratingFilterExpanded,
                        onDismissRequest = { ratingFilterExpanded = false }) {
                        ratingFilterOptions.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    ratingFilterValue = it
                                    ratingFilterExpanded = false
                                },
                                enabled = (it != ratingFilterValue),
                                text = {
                                    Row(horizontalArrangement = SpaceEvenly) {
                                        Text(it.toString())
                                        if(it == ratingFilterValue) {
                                            Icon(
                                                Icons.Rounded.Check,
                                                contentDescription = "check mark for rating selected status"
                                            )
                                        }
                                    }
                                })
                        }
                    }
                }
            }

            //
            if(movies?.isEmpty() == true) {
                Text(text = "No movies added", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
            } else {

                val filteredMovies = viewModel.filterMovies(movies ?: listOf(), ratingFilterValue, watchedFilterStatus)

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 128.dp)
                ) {
                    items(filteredMovies.size) {
                        Box(modifier = Modifier.width(130.dp)) {

                            AsyncImage(model = filteredMovies[it].posterUrl,
                                contentDescription = "movie poster image",
                                modifier = Modifier.width(128.dp)
                            )

                            if(filteredMovies[it].watched) {
                                Icon(
                                    painter = painterResource(id = R.drawable.wacthed_tick),
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )

                            }

                        }

                    }
                }

            }

        }

        SearchFAB(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(20.dp)) {
            Log.i("com.zed.wannawatch", "Search fab clicked")
        }
    }




}

@Composable
fun SearchFAB(modifier: Modifier, onclick : () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onclick
    ) {
        Icon(Icons.Rounded.Search, contentDescription = "Search FAB")
    }

}