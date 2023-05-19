package com.zed.wannawatch.services.models.tmdb.trending.movie

import kotlinx.serialization.Serializable

@Serializable
data class TrendingMovies(
    val page: Int,
    val results: List<TrendingMovieItem>,
    val total_pages: Int,
    val total_results: Int
)