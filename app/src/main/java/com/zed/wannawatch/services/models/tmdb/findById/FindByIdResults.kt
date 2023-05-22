package com.zed.wannawatch.services.models.tmdb.findById

import com.zed.wannawatch.services.models.tmdb.MovieResult
import com.zed.wannawatch.services.models.tmdb.TvResult
import kotlinx.serialization.Serializable

@Serializable
data class FindByIdResults(
    val movie_results: List<MovieResult>,
    val tv_results: List<TvResult>
)