package com.zed.wannawatch.services.api.models.tmdb.discover

data class DiscoverMovies(
    val page: Int,
    val results: List<DiscoverItem>,
    val total_pages: Int,
    val total_results: Int
)