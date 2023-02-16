package com.zed.wannawatch.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zed.wannawatch.services.models.Movie


@OptIn(ExperimentalFoundationApi::class)
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

                GridPosterItem(
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
                SectionHeader("Series")
            }

            items(tvshowItems.size) {
                GridPosterItem(watched = tvshowItems[it].watched,
                    posterUrl = tvshowItems[it].posterUrl,
                    onclick = {
                        onclick(tvshowItems[it])
                    })

            }
        }

    }
}