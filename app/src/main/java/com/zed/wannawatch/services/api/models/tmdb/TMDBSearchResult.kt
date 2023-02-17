package com.zed.wannawatch.services.api.models.tmdb
import kotlinx.serialization.Serializable

@Serializable
data class TMDBSearchResult<T>(
    val page: Int,
    val results: List<T>,
    val total_pages: Int,
    val total_results: Int
)