package com.zed.wannawatch.services.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Movie(
    @PrimaryKey val imdbID: String,
    val title: String,
    var posterUrl: String,
    var watched: Boolean = false,

    @ColumnInfo(defaultValue = "Movie") val resultType: MovieType = MovieType.Movie,

    /**
     * Defaults to -1 when no rating has been added
     */
    var rating: Int = -1,
    var notes: String = ""
): Serializable