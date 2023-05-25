package com.zed.wannawatch.ui.screens.search

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.utils.TMDBConstants
import com.zed.wannawatch.ui.ErrorState
import com.zed.wannawatch.ui.navigation.Screen
import com.zed.wannawatch.ui.utils.AnimatedImageLoader

@Composable
fun SearchScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: SearchViewModel = hiltViewModel()
) {
    Search(viewModel = viewModel, snackbarHostState = snackbarHostState) { id, type ->
        navController.navigate(Screen.DiscoverDetailScreen.route + "/$id/$type")
    }
}
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Search(
   viewModel: SearchViewModel,
   snackbarHostState: SnackbarHostState,
   itemClicked: (Int, MovieType) -> Unit
) {

    var searchString by remember  {
        mutableStateOf("")
    }
    var showClearIcon by remember {
        mutableStateOf(false)
    }


    val movieResults by viewModel.movieSearchResults.observeAsState()
    val seriesResults by viewModel.seriesSearchResults.observeAsState()

    val focusManager = LocalFocusManager.current

    val selected by viewModel.selected

    val errorState = viewModel.errorState


    LaunchedEffect(errorState) {
        when(errorState) {
            is ErrorState.Error -> {
                val action = snackbarHostState.showSnackbar(errorState.msg, "retry")
                if(action == SnackbarResult.ActionPerformed) {
                    viewModel.retry(searchString, selected)
                }
            }

            is ErrorState.NoError -> {}
        }
    }


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
                onClick = { viewModel.setSelectedType(MovieType.Movie) },
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
                onClick = { viewModel.setSelectedType(MovieType.Series) },
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


        if(viewModel.loading) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                CircularProgressIndicator()
            }

        } else {

            if(selected == MovieType.Movie) {
                // show movie results
                movieResults?.let {
                    val results = it.results
                    if(results.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp), contentAlignment = Center) {
                            Text("No Movies Found", style= MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        }
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
                                    itemClicked(results[it].id, selected)
                                }) {

                                // todo fix sizing
                                AnimatedImageLoader(url = TMDBConstants.imageBasePath + results[it].poster_path, 128.dp, 192.dp)

                            }
                        }
                    }
                }
            }
            else {
                // show series results
                seriesResults?.let {
                    val results = it.results
                    if(results.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp), contentAlignment = Center) {
                            Text("No Series Found", style= MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        }
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
                                    itemClicked(results[it].id, selected)
                                }) {
                                // todo fix sizing
                                AnimatedImageLoader(url = TMDBConstants.imageBasePath + results[it].poster_path, 128.dp, 192.dp)

                            }
                        }
                    }
                }
            }
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

