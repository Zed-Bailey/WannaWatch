package com.zed.wannawatch.services.api.models.tmdb
import kotlinx.serialization.Serializable
@Serializable
data class LastEpisodeToAir(
    val air_date: String,
    val episode_number: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val production_code: String,
    val runtime: Int?,
    val season_number: Int,
    val still_path: String?,
    val vote_average: Double,
    val vote_count: Int
)