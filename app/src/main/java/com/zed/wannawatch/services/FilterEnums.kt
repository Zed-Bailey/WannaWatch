package com.zed.wannawatch.services

enum class HomeScreenWatchedFilter(val watched: Boolean? = null) {
    All, Watched(true), Unwatched(false)
}

enum class MovieRatingFilter(val ratingValue: Int) {
    All(-1),
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5)
}