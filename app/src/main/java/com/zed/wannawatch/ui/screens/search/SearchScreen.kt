package com.zed.wannawatch.ui.screens.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.valentinilk.shimmer.shimmer
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.ui.ScaffoldState
import com.zed.wannawatch.ui.WannaWatchScaffold


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory((LocalContext.current.applicationContext as MovieApplication).repository)
    )
) {
    // todo: wannawatch text not centered when only back button
    WannaWatchScaffold(
        scaffoldState = ScaffoldState(
            shouldShowBack = true,
            onBackPressed = {
                navController.navigateUp()
            }
        )
    ) {
        Search(viewModel = viewModel)
    }
}
@OptIn(ExperimentalComposeUiApi::class)
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

    val apiResults by viewModel.results.observeAsState()

    val detailResult by viewModel.detailResult.observeAsState()

    val detailLoading = viewModel.detailLoading.observeAsState().value
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {

        SearchField(
            searchString = searchString,
            showClearIcon = showClearIcon,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(CenterHorizontally),
            onChange = {
                searchString = it
                showClearIcon = searchString.isNotEmpty()
            },
            onSearch = {
                viewModel.search(searchString)

                // close keyboard
                focusManager.clearFocus()
            },
            clearIconOnClick = {
                searchString = ""
                showClearIcon = false
            }
        )

        if(viewModel.loading) {
            // show skeleton loader
            LazyVerticalGrid(
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                columns = GridCells.Fixed(3)
            ) {
                items(20) {
                    Box(modifier = Modifier
                        .width(128.dp)
                        .height(175.dp)
                        .shimmer()
                        .background(Color.Gray)
                    ) {}
                }
            }

        } else {
            if(viewModel.error) {
                Text("something happened")
            } else {
                apiResults?.let { results ->
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
                                    viewModel.getDetail(results[it].imdbID)
                                }) {

                                AsyncImage(
                                    model = results[it].Poster,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .aspectRatio(2f/3f)
                                )
                            }
                        }
                    }
                }

            }


        }

        if(dialogOpen) {
            detailResult?.let {
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
                    SearchPopup(it, detailLoading = detailLoading) {
                        val type = if(it.Type == "movie") MovieType.Movie else MovieType.Series
                        viewModel.addMovie(
                            Movie(
                                imdbID = it.imdbID,
                                title = it.Title,
                                posterUrl = it.Poster,
                                resultType = type,
                            )
                        )
                        dialogOpen = false
                        Toast.makeText(context, "Added ${if(type == MovieType.Movie) "Movie" else "Series"}", Toast.LENGTH_SHORT).show()
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

