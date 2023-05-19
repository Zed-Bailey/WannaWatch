package com.zed.wannawatch.services.models.tmdb.trending.tv

import kotlinx.serialization.Serializable

@Serializable
data class TrendingTvShows(
    val page: Int,
    val results: List<TrendingTvShowItem>,
    val total_pages: Int,
    val total_results: Int
)