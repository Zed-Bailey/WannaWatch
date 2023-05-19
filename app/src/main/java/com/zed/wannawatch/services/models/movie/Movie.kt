package com.zed.wannawatch.services.models.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Movie(
    @PrimaryKey val imdbID: String,

    // the movie database id for the movie/series
    @ColumnInfo(defaultValue = "0") var tmdbId: Int,

    val title: String,
    var posterUrl: String,
    var watched: Boolean = false,

    @ColumnInfo(defaultValue = "Movie") val resultType: MovieType,

    @ColumnInfo(defaultValue = "-1") var year: Int,

    // when movietype == series then this should be a json serialized string of the
    // series number of seasons and number of episodes per season
    var seriesSeasons: String? = null,

    @ColumnInfo(defaultValue = "") var description: String,

    /**
     * Defaults to -1 when no rating has been added
     */
    var rating: Int = -1,
    var notes: String = ""

): Serializable