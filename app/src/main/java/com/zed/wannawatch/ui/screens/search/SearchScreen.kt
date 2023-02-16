package com.zed.wannawatch.ui.screens.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zed.wannawatch.R
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.services.repository.TMDBConstants
import com.zed.wannawatch.ui.LottieAnimatedView
import com.zed.wannawatch.ui.ScaffoldState
import com.zed.wannawatch.ui.WannaWatchScaffold
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory((LocalContext.current.applicationContext as MovieApplication).repository)
    )
) {

    WannaWatchScaffold(
        scaffoldState = ScaffoldState(
            shouldShowBack = true,
            onBackPressed = {
                navController.navigateUp()
            },
            actions = { }
        )
    ) {
        Search(viewModel = viewModel)
    }

}
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Search(
   viewModel: SearchViewModel
) {

    var searchString by remember  {
        mutableStateOf("")
    }
    var showClearIcon by remember {
        mutableStateOf(false)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val movieResults by viewModel.movieSearchResults.observeAsState()
    val seriesResults by viewModel.seriesSearchResults.observeAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var selected by remember { mutableStateOf(MovieType.Movie) }

    val detailLoading by viewModel.detailLoading.observeAsState()

    val seriesDetail = viewModel.seriesDetail.observeAsState()
    val movieDetail = viewModel.movieDetail.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {



            SearchField(
                searchString = searchString,
                showClearIcon = showClearIcon,

                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 5.dp)
                    .align(CenterHorizontally),

                onChange = {
                    searchString = it
                    showClearIcon = searchString.isNotEmpty()
                },
                onSearch = {
                    viewModel.search(searchString, selected)

                    // close keyboard
                    focusManager.clearFocus()
                },
                clearIconOnClick = {
                    searchString = ""
                    showClearIcon = false
                }
            )



        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {

            Text("I'm looking for a ", modifier = Modifier.align(CenterVertically), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(5.dp))
            FilterChip(
                selected = selected == MovieType.Movie,
                onClick = { selected = MovieType.Movie },
                label = { Text("Movie") },
                leadingIcon = if (selected == MovieType.Movie) {
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

            Spacer(Modifier.width(5.dp))

            FilterChip(
                selected = selected == MovieType.Series,
                onClick = { selected = MovieType.Series },
                label = { Text("Series") },
                leadingIcon = if (selected == MovieType.Series) {
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


        if(movieResults == null && seriesResults == null) {
            val randomChance by remember {
                mutableStateOf(Random.nextFloat() <= 0.05f)
            }

            if(randomChance) {
                LottieAnimatedView(resId = R.raw.lottie_dancing_duck)
            }
        }

        if(viewModel.loading) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                LottieAnimatedView(resId = R.raw.lottie_hand_loading)
            }

        } else {

            if(selected == MovieType.Movie) {
                // show movie results
                movieResults?.let {
                    val results = it.results
                    if(results.isEmpty()) {
                        Text("No results :(")
                    }
                    LazyVerticalGrid(
                        userScrollEnabled = true,
                        state = rememberLazyGridState(),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .align(CenterHorizontally),
                        columns = GridCells.Fixed(3)
                    ){
                        items(results.size) {
                            Box(modifier = Modifier
                                .width(128.dp)
                                .padding(5.dp)
                                .clickable {
                                    dialogOpen = true
                                    viewModel.getDetail(results[it].id, selected)
                                }) {

                                AnimatedImageLoader(url = TMDBConstants.imageBasePath + results[it].poster_path, 128.dp, 192.dp)

                            }
                        }
                    }
                }
            } else {
                // show series results
                seriesResults?.let {
                    val results = it.results
                    if(results.isEmpty()) {
                        Text("No results :(")
                    }
                    LazyVerticalGrid(
                        userScrollEnabled = true,
                        state = rememberLazyGridState(),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .align(CenterHorizontally),
                        columns = GridCells.Fixed(3)
                    ){
                        items(results.size) {
                            Box(modifier = Modifier
                                .width(128.dp)
                                .padding(5.dp)
                                .clickable {
                                    dialogOpen = true
                                    viewModel.getDetail(results[it].id, selected)
                                }) {

                                AnimatedImageLoader(url = TMDBConstants.imageBasePath + results[it].poster_path, 128.dp, 192.dp)

                            }
                        }
                    }
                }
            }
        }

        if(dialogOpen) {

            Dialog(
                // dialog doesn't change shape based on dynamic content in recomposition
                // known bug and can be fixed with the usePlatformDefaultWidth value  in dialog properties
                // https://issuetracker.google.com/issues/221643630#comment9
                // https://stackoverflow.com/a/72018943
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = {
                    dialogOpen = false
                }
            ) {

                if(selected == MovieType.Movie) {
                    movieDetail.value?.let {
                        MovieDetailDialog(
                            model = it,
                            detailLoading = detailLoading,
                            onClose = {
                                dialogOpen = false
                            },
                            onAdd =  {
                                val movie = Movie(
                                    imdbID = it.imdb_id!!,
                                    tmdbId = it.id,
                                    title = it.title,
                                    description = it.overview ?: it.tagline ?: "No description provided",
                                    year = it.release_date.take(4).toInt(),
                                    resultType = MovieType.Movie,
                                    posterUrl = TMDBConstants.imageBasePath + (it.poster_path ?: it.backdrop_path)
                                )
                                dialogOpen = false
                                viewModel.addModel(movie)
                                Toast.makeText(context, "Added Movie", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                } else if(selected == MovieType.Series) {
                    seriesDetail.value?.let {
                        SeriesDetailDialog(
                            model = it,
                            detailLoading = detailLoading,
                            onAdd =  {
                                val movie = Movie(
                                    imdbID = "",
                                    tmdbId = it.id,
                                    title = it.name,
                                    description = it.overview,
                                    year = it.first_air_date.take(4).toInt(),
                                    resultType = MovieType.Series,
                                    posterUrl = TMDBConstants.imageBasePath + (it.poster_path ?: it.backdrop_path),
                                    seriesSeasons = Json.encodeToString(value = it.seasons),
                                )
                                dialogOpen = false
                                viewModel.addModel(movie)
                                Toast.makeText(context, "Added Series", Toast.LENGTH_SHORT).show()
                            },
                            onClose = {
                                dialogOpen = false
                            }
                        )
                    }
                }

            }
        }

        if(viewModel.error) {
            Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_LONG).show()
        }


    }

}








@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(searchString: String, showClearIcon: Boolean, modifier: Modifier, onChange: (String) -> Unit, onSearch: (KeyboardActionScope) -> Unit, clearIconOnClick: () -> Unit) {
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(MaterialTheme.colorScheme.primary) ,
        placeholder = { Text("Search for a movie or show") },
        modifier = modifier,
        value = searchString,
        onValueChange = onChange,
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = null)
        },
        trailingIcon = {
            if(showClearIcon) {
                IconButton(onClick = clearIconOnClick) {
                    Icon(Icons.Rounded.Clear, contentDescription = null)
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = onSearch
        )
    )
}

