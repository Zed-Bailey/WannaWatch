package com.zed.wannawatch.services.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchData(
    @SerializedName("Search")
    val results: MutableList<SearchResult>
)

data class SearchResult (
    val imdbID: String,

    @SerializedName("Title")
    val title: String,

    @SerializedName("Year")
    val year: String,

    @SerializedName("Type")
    val type: String,

    @SerializedName("Poster")
    val poster: String
): Serializable {}
