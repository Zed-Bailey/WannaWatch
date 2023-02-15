package com.zed.wannawatch.ui.screens.watch


import android.webkit.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.web.*
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.api.models.seapi.ServerResult
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.ui.ScaffoldState
import com.zed.wannawatch.ui.WannaWatchScaffold

//todo add scaffold for back button

@Composable
fun WatchScreen(navController: NavController,
                imdbId: String,
                viewModel: WatchViewModel = viewModel()
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getModel((context.applicationContext as MovieApplication).repository, imdbId)
    }

    WannaWatchScaffold(
        scaffoldState = ScaffoldState(
            shouldShowBack = true,
            onBackPressed = {
                navController.navigateUp()
            }
        )
    ) {
        Watch(viewModel = viewModel)
    }


}


@Composable
fun Watch(
    viewModel: WatchViewModel
) {
    val context = LocalContext.current

//    LaunchedEffect(Unit) {
//        viewModel.getModel((context.applicationContext as MovieApplication).repository, imdbId)
//    }

    val loading = viewModel.loading

    val serversLoading = viewModel.serversLoading


    val model = viewModel.model.observeAsState()


    val servers = viewModel.servers.observeAsState()

    val seasons = viewModel.seasons.observeAsState()


    var selectedSeason by remember {
        mutableStateOf(1)
    }

    var selectedEpisode by remember {
        mutableStateOf(1)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
    ) {
        if (loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            model.value?.let { movie ->
                // shows the episode/season dropdowns
                if (movie.resultType == MovieType.Series && seasons.value != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        SeasonList(
                            options = seasons.value!!.map { it.season_number },
                            currentlySelected = selectedSeason,
                            onSelected = { season ->
                                selectedSeason = season
                                selectedEpisode = 1
                                viewModel.clearServers()
                            }
                        )

                        Spacer(Modifier.width(20.dp))

                        EpisodeList(
                            episodeCount = seasons.value!!.first { it.season_number == selectedSeason }.episode_count,
                            currentlySelected = selectedEpisode,
                            onSelected = { episode ->
                                selectedEpisode = episode
                                viewModel.clearServers()
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        if(movie.resultType == MovieType.Movie) {
                            viewModel.getMovieServers(movie.imdbID)
                        } else {
                            viewModel.getSeriesServer(movie.imdbID, selectedSeason, selectedEpisode)
                        }
                    },
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Icon(Icons.Rounded.Search, null)
                    Spacer(Modifier.width(10.dp))
                    Text("Find servers")
                }

            }
        }


        servers.value?.let { s ->

            Text("Servers", style = MaterialTheme.typography.headlineMedium)
            Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                ) {

                s.results.forEach {

                    ServerListElement(server = it) {url ->
                        viewModel.openCustomTab(url, context)
                    }

                }
            }


        }


        Box(Modifier.fillMaxSize()) {

            if(serversLoading) {
                CircularProgressIndicator(
                    Modifier.align(Center)
                )
            }



        }





    }
}


@Composable
fun ServerListElement(server: ServerResult, onClick: (String) -> Unit) {
    FilledTonalButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ,
        shape = RoundedCornerShape(13.dp),
        onClick = {
            onClick(server.url)
        }
    ) {
        Box() {

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(server.server, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(3.dp))
                Text(server.quality, style = MaterialTheme.typography.bodySmall)
            }

            Icon(
                Icons.Rounded.ArrowRight,
                contentDescription = null,
                modifier = Modifier.align(
                    CenterEnd
                )
            )

        }





    }
}

@Preview
@Composable
fun ServerListElementPreview() {
    ServerListElement(
        server = ServerResult(
            server = "server name",
            title = "movie title",
            exact_match = 1,
            quality = "720p",
            size = 1234,
            url = ""
        )
    ) {
        // would open url here
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerItem(model: ServerResult, selectedServer: ServerResult?, onSelected: () -> Unit) {

    val selected = model == selectedServer
    FilterChip(
        modifier = Modifier.padding(horizontal = 2.dp),
        selected = selected,
        onClick = onSelected,
        label = { Text("${model.server} - ${model.quality}") },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )

}


@Composable
fun SeasonList(options: List<Int>, currentlySelected: Int, onSelected: (Int) -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box() {
        Button(
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            onClick = {
                expanded = true
            }
        ) {
            Text(text = "Season $currentlySelected", color = MaterialTheme.colorScheme.onSurface)
//            Icon(Icons.Rounded.ArrowDropDown, contentDescription = "drop down arrow")
            Icon(
                if (expanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    onClick = {
                        onSelected(it)
                        expanded = false
                    },
                    enabled = (it != currentlySelected),
                    text = {
                        Text(it.toString())
                    }, trailingIcon = {
                        if (it == currentlySelected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = "check mark for watched status selected"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EpisodeList(episodeCount: Int, currentlySelected: Int, onSelected: (Int) -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val episodes = List(episodeCount) { it + 1 }

    Box() {
        Button(
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            onClick = {
                expanded = true
            }
        ) {
            Text(text = "Episode $currentlySelected", color = MaterialTheme.colorScheme.onSurface)
            Icon(
                if (expanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                contentDescription = "drop down arrow"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            episodes.forEach {
                DropdownMenuItem(
                    onClick = {
                        onSelected(it)
                        expanded = false
                    },
                    enabled = (it != currentlySelected),
                    text = {
                        Text(it.toString())
                    }, trailingIcon = {
                        if (it == currentlySelected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = "check mark for watched status selected"
                            )
                        }
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun WatchPreview() {
    WatchScreen(navController = rememberNavController(), imdbId = "tt0468569")
}