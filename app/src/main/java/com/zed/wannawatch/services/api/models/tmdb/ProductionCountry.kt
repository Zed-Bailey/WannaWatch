package com.zed.wannawatch.services.api.models.tmdb
import kotlinx.serialization.Serializable
@Serializable
data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)