package com.zed.wannawatch.ui.screens.search

import com.zed.wannawatch.services.models.tmdb.MovieResult
import com.zed.wannawatch.services.models.tmdb.TMDBSearchResult
import com.zed.wannawatch.services.models.tmdb.TvResult

sealed class SearchState {
    object Nothing: SearchState()
    object Loading: SearchState()
    data class SeriesSearchSuccess(val data: TMDBSearchResult<TvResult>) : SearchState()
    data class MovieSearchSuccess(val data: TMDBSearchResult<MovieResult>) : SearchState()
    data class Error(val message: String) : SearchState()
}
