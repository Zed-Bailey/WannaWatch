package com.zed.wannawatch.services.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Movie(
    @PrimaryKey val imdbID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "posterUrl") var posterUrl: String,
    @ColumnInfo(name = "watched") var watched: Boolean = false,

    /**
     * Defaults to -1 when no rating has been added
     */
    @ColumnInfo(name = "rating") var rating: Int = -1,
    @ColumnInfo(name = "notes") var notes: String = ""
): Serializable