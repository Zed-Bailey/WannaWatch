package com.zed.wannawatch.ui.screens.discover

import com.zed.wannawatch.services.models.tmdb.trending.movie.TrendingMovies
import com.zed.wannawatch.services.models.tmdb.trending.tv.TrendingTvShows

sealed class DiscoverState {
    object Loading : DiscoverState()

    data class Success(val movies: TrendingMovies, val series: TrendingTvShows): DiscoverState()

    data class Error(val message: String): DiscoverState()

}
