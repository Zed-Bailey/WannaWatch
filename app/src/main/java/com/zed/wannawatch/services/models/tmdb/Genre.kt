package com.zed.wannawatch.services.models.tmdb
import kotlinx.serialization.Serializable
@Serializable
data class Genre(
    val id: Int,
    val name: String
)