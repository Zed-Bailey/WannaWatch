package com.zed.wannawatch.services.models.tmdb.discover

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverMovies(
    val page: Int,
    val results: List<DiscoverItem>,
    val total_pages: Int,
    val total_results: Int
)