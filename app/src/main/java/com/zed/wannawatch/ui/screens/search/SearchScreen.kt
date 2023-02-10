package com.zed.wannawatch.ui.screens.search

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.valentinilk.shimmer.shimmer
import com.zed.wannawatch.services.api.models.SearchDetail

@Composable
fun SearchScreen(viewModel: SearchViewModel, itemClicked: () -> Unit) {

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
                verticalArrangement = Arrangement.spacedBy(20.dp),
                columns = GridCells.Fixed(3)
            ) {
                items(20) {
                    Box(modifier = Modifier
                        .width(128.dp)
                        .height(150.dp)
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
                    Text (
                        "${results.size} results",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                    )

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
                                .clickable {
                                    Log.i("com.zed.wannawatch", "clicked ${results[it].Title}")
                                    dialogOpen = true
                                    viewModel.getDetail(results[it].imdbID)
                                }) {

                                AsyncImage(model = results[it].Poster, contentDescription = null)
                            }
                        }
                    }
                }

            }


        }

        if(dialogOpen) {
            detailResult?.let {
                Dialog(onDismissRequest = {
                    dialogOpen = false

                }) {
                    SearchPopup(it) {
                        Log.i("com.zed.wannawatch", "will add ${it.Title} for you")
                        dialogOpen = false
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

@Composable
fun SearchPopup(selectedResult: SearchDetail, onAdd: () -> Unit) {
    // crashing on this line
    val genres = selectedResult.Genre.split(",")

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
            AsyncImage(
                model = selectedResult.Poster,
                contentDescription = "poster image for ${selectedResult.Title}",
                modifier = Modifier
                    .width(128.dp)
                    .height(170.dp)
                    .align(CenterHorizontally)
            )


            Text(selectedResult.Title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(vertical = 10.dp)
            )

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            ) {
                Text("${selectedResult.Runtime} watch time")
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

            Text(selectedResult.Plot, style = MaterialTheme.typography.bodyMedium)
            
            Button(
                onClick = onAdd,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(CenterHorizontally)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "I Wanna Watch!")
            }

        }



    }
}