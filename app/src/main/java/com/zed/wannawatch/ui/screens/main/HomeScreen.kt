package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zed.wannawatch.R
import com.zed.wannawatch.services.HomeScreenWatchedFilter
import com.zed.wannawatch.services.MovieRatingFilter
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType


@Composable
fun HomeScreen(viewModel: MainViewModel, movieClicked: (Movie) -> Unit, searchClicked: () -> Unit) {

    val movies by viewModel.movies.observeAsState()

    var watchedFilterStatus by remember { mutableStateOf(HomeScreenWatchedFilter.All) }
    val watchedFilterOptions = listOf(HomeScreenWatchedFilter.All, HomeScreenWatchedFilter.Watched, HomeScreenWatchedFilter.Unwatched)
    var watchedFilterExpanded by remember { mutableStateOf(false) }

    var ratingFilterValue by remember { mutableStateOf(MovieRatingFilter.All) }
    val ratingFilterOptions = listOf(MovieRatingFilter.All, MovieRatingFilter.One, MovieRatingFilter.Two, MovieRatingFilter.Three, MovieRatingFilter.Four, MovieRatingFilter.Five)
    var ratingFilterExpanded by remember { mutableStateOf(false) }

    val listState = rememberLazyGridState()


    val expandedFabState = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    LaunchedEffect(expandedFabState.value) {
        viewModel.fabExpanded.value = expandedFabState.value
    }

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
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        onClick = {
                            watchedFilterExpanded = true
                        }
                    ) {
                        Text(text = "filter by watched", color = Color.White)
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
                                    Text(it.toString())
                                }, trailingIcon = {
                                    if(it == watchedFilterStatus) {
                                        Icon(
                                            Icons.Rounded.Check,
                                            contentDescription = "check mark for watched status selected"
                                        )
                                    }
                                })
                        }
                    }
                }

                // filter movies by rating
                Box(){
                    Button(
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        onClick = {
                            ratingFilterExpanded = true
                        }
                    ) {
                        Text(text = "filter by rating", color = Color.White)
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
                                    if(it.ratingValue == -1 ) {
                                        Text(it.toString())
                                    } else {
                                        Text(it.toString() + if (it.ratingValue == 1) " star" else " stars")
                                    }
                                }, trailingIcon = {
                                    if(it == ratingFilterValue) {
                                        Icon(
                                            Icons.Rounded.Check,
                                            contentDescription = "check mark for rating selected status"
                                        )
                                    }
                                })
                        }
                    }
                }
            }

            //
            if(movies?.isEmpty() == true) {
                Text(text = "No movies or tv-shows added", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
            } else {
                // filters results to
                val filteredMovies = viewModel.filterMovies(movies ?: listOf(), ratingFilterValue, watchedFilterStatus)
                val moviesOnly = viewModel.filterResultsToType(filteredMovies, MovieType.Movie)
                val seriesOnly = viewModel.filterResultsToType(filteredMovies, MovieType.Series)

                ResultsGrid(
                    movieItems = moviesOnly,
                    tvshowItems = seriesOnly,
                    listState = listState,
                    onclick = movieClicked
                )
            }
        }


//        https://itnext.io/floating-action-button-in-jetpack-compose-with-material-3-10ba8bff415a
        SearchFAB(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(20.dp),
            expanded = viewModel.fabExpanded.value
        ) {
            searchClicked()
        }
    }
}

@Composable
fun ResultsGrid(movieItems: List<Movie>, tvshowItems: List<Movie>, listState: LazyGridState, onclick: (Movie) -> Unit) {
    LazyVerticalGrid(
        state = listState,
        userScrollEnabled = true ,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize(),
//                    columns = GridCells.Adaptive( = 128.dp)
        columns = GridCells.Fixed(3)
    ) {
        if(movieItems.isNotEmpty()) {
            item(span = { GridItemSpan(this.maxLineSpan) } ) {
                SectionHeader("Movies")
            }

            items(movieItems.size) {
                GridItem(watched = movieItems[it].watched,
                    posterUrl = movieItems[it].posterUrl,
                    onclick = {
                        onclick(movieItems[it])
                    })

            }
        }

        if (tvshowItems.isNotEmpty()) {
            item(span = { GridItemSpan(this.maxLineSpan) } ){
                SectionHeader("Tv Shows")
            }

            items(tvshowItems.size) {
                GridItem(watched = tvshowItems[it].watched,
                    posterUrl = tvshowItems[it].posterUrl,
                    onclick = {
                        onclick(tvshowItems[it])
                    })

            }
        }

    }
}

// TODO component needs a better name
@Composable
fun GridItem(watched: Boolean, posterUrl: String, onclick: () -> Unit) {

    Box(
        modifier = Modifier
        .width(128.dp)
        .clickable {
            onclick()
        }
    ) {

        AsyncImage(model = posterUrl,
            contentDescription = "movie poster image",
            modifier = Modifier.width(128.dp),
        )

        if(watched) {
            Icon(
                // TODO fix watched tick name
                painter = painterResource(id = R.drawable.wacthed_tick),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd),
                tint = Color.Unspecified
            )

        }

    }
}

@Composable
fun SectionHeader(text: String) {
    Column() {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 10.dp),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Divider(color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(horizontal = 10.dp), thickness = 1.dp)
    }

}

@Composable
fun SearchFAB(modifier: Modifier, expanded: Boolean, onclick : () -> Unit) {

    ExtendedFloatingActionButton(
        modifier = modifier,
        text = {
            Text(text = "Search")
        },
        icon = {
            Icon(Icons.Rounded.Search, contentDescription = "Search FAB")
        },
        onClick = onclick,
        expanded = expanded,
    )

}