package com.zed.wannawatch.services.api.models.seapi

@kotlinx.serialization.Serializable
data class Servers(
    val message: String,
    val results: List<ServerResult>,
    val status: Int,
    val title: String
)