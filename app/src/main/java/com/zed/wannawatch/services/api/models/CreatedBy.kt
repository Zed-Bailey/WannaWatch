package com.zed.wannawatch.services.api.models
import kotlinx.serialization.Serializable
@Serializable
data class CreatedBy(
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val profile_path: String?
)