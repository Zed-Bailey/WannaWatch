package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zed.wannawatch.R
import com.zed.wannawatch.services.HomeScreenWatchedFilter
import com.zed.wannawatch.services.MediaTypeFilter
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.MovieRatingFilter
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.ui.ScaffoldState
import com.zed.wannawatch.ui.WannaWatchScaffold
import com.zed.wannawatch.ui.navigation.Screen


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory((LocalContext.current.applicationContext as MovieApplication).repository)
    )
) {

    WannaWatchScaffold(scaffoldState = ScaffoldState()) {
        Home(
            viewModel = viewModel,
            movieClicked = {
                navController.navigate(Screen.DetailScreen.route + "/${it}")
            },
            searchClicked = {
                navController.navigate(Screen.SearchScreen.route)
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: HomeViewModel,
    movieClicked: (String) -> Unit,
    searchClicked: () -> Unit
) {

    val movies by viewModel.movies.collectAsState(initial = listOf())

    var watchedFilterStatus by remember { mutableStateOf(HomeScreenWatchedFilter.All) }
    val watchedFilterOptions = listOf(HomeScreenWatchedFilter.All, HomeScreenWatchedFilter.Watched, HomeScreenWatchedFilter.Unwatched)
    var watchedFilterExpanded by remember { mutableStateOf(false) }

    var ratingFilterValue by remember { mutableStateOf(MovieRatingFilter.All) }
    val ratingFilterOptions = listOf(MovieRatingFilter.All, MovieRatingFilter.One, MovieRatingFilter.Two, MovieRatingFilter.Three, MovieRatingFilter.Four, MovieRatingFilter.Five)
    var ratingFilterExpanded by remember { mutableStateOf(false) }

    var mediaTypeFilterValue by remember { mutableStateOf(MediaTypeFilter.AllMedia) }
    val mediaTypeFilterOptions = listOf(MediaTypeFilter.AllMedia, MediaTypeFilter.MovieMedia, MediaTypeFilter.SeriesMedia)

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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Icon(Icons.Rounded.FilterList, null)
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text("Filter by")

                Spacer(modifier = Modifier.width(20.dp))
                // filter movies by watched/unwatched status
                Box(){
                    Button(
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                        onClick = {
                            watchedFilterExpanded = true
                        }
                    ) {
                        Text(text = "watched", color = MaterialTheme.colorScheme.onSurface)
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

                Spacer(modifier = Modifier.width(10.dp))

                // filter movies by rating
                Box(){
                    Button(
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                        onClick = {
                            ratingFilterExpanded = true
                        }
                    ) {
                        Text(text = "rating", color = MaterialTheme.colorScheme.onSurface)
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 5.dp),
            ) {
                mediaTypeFilterOptions.forEach { option ->
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        selected = (mediaTypeFilterValue == option),
                        onClick = {
                            mediaTypeFilterValue = option
                        },
                        label = {
                            when(option) {
                                MediaTypeFilter.AllMedia -> Text("All")
                                MediaTypeFilter.MovieMedia -> Text("Movie")
                                MediaTypeFilter.SeriesMedia -> Text("Series")
                            }
                        },
                        leadingIcon = if (mediaTypeFilterValue == option) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        }

                    )
                }


            }


            //
            if(movies.isEmpty()) {
                Text(text = "No movies or tv-shows added", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize(),)
            } else {
                // filters results to
                val filteredMovies = viewModel.filterMovies(movies, ratingFilterValue, watchedFilterStatus, mediaTypeFilterValue)
                val moviesOnly = viewModel.filterResultsToType(filteredMovies, MovieType.Movie)
                val seriesOnly = viewModel.filterResultsToType(filteredMovies, MovieType.Series)

                ResultsGrid(
                    movieItems = moviesOnly,
                    tvshowItems = seriesOnly,
                    listState = listState,
                    onclick = {
                        movieClicked(it.imdbID)
                    }
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
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        columns = GridCells.Fixed(3)
    ) {
        if(movieItems.isNotEmpty()) {
            item(span = { GridItemSpan(this.maxLineSpan) } ) {
                SectionHeader("Movies")
            }

            items(movieItems.size) {
                GridItem(
                    watched = movieItems[it].watched,
                    posterUrl = movieItems[it].posterUrl,
                    onclick = {
                        onclick(movieItems[it])
                    }
                )
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
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .width(128.dp)
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
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
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