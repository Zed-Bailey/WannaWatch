package com.zed.wannawatch.services.api.models.seapi

@kotlinx.serialization.Serializable
data class ServerResult(
    val exact_match: Int,
    val quality: String,
    val server: String,
    val title: String,
    val url: String
)