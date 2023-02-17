package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zed.wannawatch.services.HomeScreenWatchedFilter
import com.zed.wannawatch.services.MediaTypeFilter
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.MovieRatingFilter
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

    WannaWatchScaffold(scaffoldState = ScaffoldState(
        actions = {}
    )) {
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

//    val movies by viewModel.movies.collectAsState(initial = listOf())

    val movies by viewModel.movies.observeAsState()
    val dataLoading = viewModel.dataLoading


    var watchedFilterStatus by remember { mutableStateOf(HomeScreenWatchedFilter.All) }


    var ratingFilterValue by remember { mutableStateOf(MovieRatingFilter.All) }


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

    LaunchedEffect(Unit) {
        viewModel.getAllMovies()
    }

    Box () {
        Column(modifier = Modifier.fillMaxSize()) {
            // filter options
            // todo: export to component
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Spacer(modifier = Modifier.width(20.dp))

                // filter movies by watched/unwatched status
                WatchedFilterDropdown(
                    currentlySelected = watchedFilterStatus,
                    onSelect = {
                        watchedFilterStatus = it
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // filter movies by rating
                RatingFilterDropDown(
                    currentlySelected = ratingFilterValue,
                    onSelect = {
                        ratingFilterValue = it
                    }
                )


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
                                MediaTypeFilter.MovieMedia -> Text("Movies only")
                                MediaTypeFilter.SeriesMedia -> Text("Series only")
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
            if(dataLoading.value) {
                Box(Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator()
                }
            }
            else if (movies?.isEmpty() == true) {
                Box(Modifier.fillMaxWidth().padding(top = 50.dp), contentAlignment = Center) {
                    Text("No Movies or Series added", style= MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
                }

            } else {
                movies?.let {
                    // filters results based on the type and filter paramters
                    val filteredMovies = viewModel.filterMovies(it, ratingFilterValue, watchedFilterStatus, mediaTypeFilterValue)
                    val moviesOnly = viewModel.filterResultsToType(filteredMovies, MovieType.Movie)
                    val seriesOnly = viewModel.filterResultsToType(filteredMovies, MovieType.Series)

                    ResultsGrid(
                        movieItems = moviesOnly,
                        tvshowItems = seriesOnly,
                        listState = listState,
                        onclick = { m ->
                            movieClicked(m.imdbID)
                        }
                    )
                }
            }
        }



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
fun WatchedFilterDropdown(currentlySelected: HomeScreenWatchedFilter, onSelect: (HomeScreenWatchedFilter) -> Unit) {
    val watchedFilterOptions = listOf(HomeScreenWatchedFilter.All, HomeScreenWatchedFilter.Watched, HomeScreenWatchedFilter.Unwatched)
    var watchedFilterExpanded by remember { mutableStateOf(false) }

    Box(){
        Button(
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            onClick = {
                watchedFilterExpanded = true
            }
        ) {
            Icon(Icons.Rounded.FilterAlt, null, modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))

            Text(text = "Watched", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))

            Icon(if (watchedFilterExpanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, contentDescription = "drop down arrow")
        }

        DropdownMenu(
            expanded = watchedFilterExpanded,
            onDismissRequest = { watchedFilterExpanded = false }) {
            watchedFilterOptions.forEach {
                DropdownMenuItem(
                    onClick = {
                        watchedFilterExpanded = false
                        onSelect(it)
                    },
                    enabled = (it != currentlySelected),
                    text = {
                        Text(it.toString())
                    }, trailingIcon = {
                        if(it == currentlySelected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = "check mark for watched status selected"
                            )
                        }
                    })
            }
        }
    }
}


@Composable
fun RatingFilterDropDown(currentlySelected: MovieRatingFilter, onSelect: (MovieRatingFilter) -> Unit) {
    val ratingFilterOptions = listOf(MovieRatingFilter.All, MovieRatingFilter.One, MovieRatingFilter.Two, MovieRatingFilter.Three, MovieRatingFilter.Four, MovieRatingFilter.Five)
    var ratingFilterExpanded by remember { mutableStateOf(false) }

    Box(){
        Button(
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            onClick = {
                ratingFilterExpanded = true
            }
        ) {
            Icon(Icons.Rounded.FilterAlt, null, modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))

            Text(text = "Rating", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Icon(if (ratingFilterExpanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, contentDescription = "drop down arrow")
        }

        DropdownMenu(
            expanded = ratingFilterExpanded,
            onDismissRequest = { ratingFilterExpanded = false }) {
            ratingFilterOptions.forEach {
                DropdownMenuItem(
                    onClick = {
                        ratingFilterExpanded = false
                        onSelect(it)
                    },
                    enabled = (it != currentlySelected),
                    text = {
                        if(it.ratingValue == -1 ) {
                            Text(it.toString())
                        } else {
                            Text(it.toString() + if (it.ratingValue == 1) " star" else " stars")
                        }
                    }, trailingIcon = {
                        if(it == currentlySelected) {
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

