package com.zed.wannawatch.services

import com.zed.wannawatch.services.models.MovieType

// todo rename
enum class HomeScreenWatchedFilter(val watched: Boolean? = null) {
    All, Watched(true), Unwatched(false)
}

// todo rename
enum class MovieRatingFilter(val ratingValue: Int) {
    All(-1),
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5)
}

// todo rename
enum class MediaTypeFilter(val type: MovieType?) {
    AllMedia(null),
    MovieMedia(MovieType.Movie),
    SeriesMedia(MovieType.Series)
}