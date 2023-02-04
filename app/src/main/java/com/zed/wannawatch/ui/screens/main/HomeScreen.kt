package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class HomeScreenWatchedFilter(val watched: Boolean? = null) {
    All, Watched(true), Unwatched(false)
}

enum class MovieRatingFilter(val ratingValue: Int) {
    All(-1),
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5)
}


@Composable
fun HomeScreen(viewModel: MainViewModel) {

    val movies by viewModel.movies.observeAsState()

    var watchedFilterStatus by remember { mutableStateOf(HomeScreenWatchedFilter.All) }
    val watchedFilterOptions = listOf<HomeScreenWatchedFilter>(HomeScreenWatchedFilter.All, HomeScreenWatchedFilter.Watched, HomeScreenWatchedFilter.Unwatched)
    var watchedFilterExpanded by remember { mutableStateOf(false) }

    var ratingfilterValue by remember { mutableStateOf(MovieRatingFilter.All) }
    val ratingFilterOptions = listOf<MovieRatingFilter>(MovieRatingFilter.All, MovieRatingFilter.One, MovieRatingFilter.Two, MovieRatingFilter.Three, MovieRatingFilter.Four, MovieRatingFilter.Five)
    var ratingFilterExpanded by remember { mutableStateOf(false) }


    Column(modifier = Modifier.fillMaxSize()) {
        // filter rows
        Row(modifier = Modifier.fillMaxWidth()) {

            // filter movies by watched/unwatched status
            Box(){
                Button(onClick = { watchedFilterExpanded = true }) {
                    Text(text = "filter watched")
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
                                    Icon(Icons.Rounded.Check, contentDescription = "check mark for filter selected status")
                                }
                        })
                    }
                }
            }

            // filter movies by rating
            Box(){
                Button(onClick = { ratingFilterExpanded = true }) {
                    Text(text = "filter by rating")
                }

                DropdownMenu(
                    expanded = ratingFilterExpanded,
                    onDismissRequest = { ratingFilterExpanded = false }) {
                    ratingFilterOptions.forEach {
                        DropdownMenuItem(
                            onClick = {
                                ratingfilterValue = it
                                ratingFilterExpanded = false
                            },
                            enabled = (it != ratingfilterValue),
                            text = {
                                Row(horizontalArrangement = SpaceBetween) {
                                    Text(it.toString())
                                    if(it == ratingfilterValue) {
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

            val filteredMovies = movies!!.filter {
                // filter by watched status
                var result = false

                result = if(watchedFilterStatus == HomeScreenWatchedFilter.All)
                    true
                else
                    watchedFilterStatus.watched == it.watched

                result
            }.filter {
                it.rating >= ratingfilterValue.ratingValue
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(filteredMovies.size) {
                    
                }
            }

        }
    }

}