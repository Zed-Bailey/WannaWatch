package com.zed.wannawatch.services.api.models
import kotlinx.serialization.Serializable

@Serializable
data class TvExternalIds(
    val facebook_id: String,
    val freebase_id: String,
    val freebase_mid: String,
    val id: Int,
    val imdb_id: String,
    val instagram_id: String,
    val tvdb_id: Int,
    val tvrage_id: Int,
    val twitter_id: String
)