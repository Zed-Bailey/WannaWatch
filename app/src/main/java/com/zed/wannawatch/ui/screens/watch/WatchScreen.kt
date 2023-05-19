package com.zed.wannawatch.ui.screens.watch


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zed.wannawatch.R
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.api.models.seapi.ServerResult
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.ui.LottieAnimatedView


@Composable
fun WatchScreen(navController: NavController,
                imdbId: String,
                viewModel: WatchViewModel = viewModel(
                    factory = WatchViewModelFactory((LocalContext.current.applicationContext as MovieApplication).repository)
                )
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getModel((context.applicationContext as MovieApplication).repository, imdbId)
    }

//    WannaWatchScaffold(
//        scaffoldState = ScaffoldState(
//            shouldShowBack = true,
//            onBackPressed = {
//                navController.navigateUp()
//            },
//            actions = {
//                viewModel.model.value?.let {
//                    if(it.resultType == MovieType.Series) {
//                        // show seasons refresh action item
//                        RefreshActionItem {
//                            if(it.tmdbId != 0) {
//                                viewModel.refreshSeasons(it.tmdbId)
//                            } else {
//                                Toast.makeText(context, "Error: please re-add this series", Toast.LENGTH_LONG).show()
//                            }
//                        }
//                    }
//                }
//            }
//        )
//    ) {
//        Watch(viewModel = viewModel)
//    }


}


@Composable
fun Watch(
    viewModel: WatchViewModel
) {
    val context = LocalContext.current

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
            // model loading indicator
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
                            viewModel.getServers(movie.imdbID)
                        } else {
                            viewModel.getServers(movie.imdbID, selectedSeason, selectedEpisode)
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

            if(s.results.isEmpty()) {

                Box(Modifier.fillMaxWidth().padding(top = 50.dp), contentAlignment = Center) {
                    Text("No Servers Found", style= MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }


            } else {

                Text("Servers", style = MaterialTheme.typography.headlineMedium)
                Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 1.dp)



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(Modifier.height(15.dp))

                    viewModel.filterServers(s.results).forEach {

                        ServerListElement(server = it) {url ->
                            viewModel.openCustomTab(url, context)
                        }

                    }

                    Spacer(Modifier.height(15.dp))
                }

            }
        }


        Box(Modifier.fillMaxSize()) {

            if(serversLoading) {
                Box(Modifier.align(Center)) {
                    LottieAnimatedView(resId = R.raw.lottie_hand_loading)
                }
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
            url = ""
        )
    ) {
        // would open url here
    }
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
            onDismissRequest = { expanded = false },
            properties = PopupProperties()

        ) {

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