package com.zed.wannawatch.services.api.models
import kotlinx.serialization.Serializable
@Serializable
data class Genre(
    val id: Int,
    val name: String
)