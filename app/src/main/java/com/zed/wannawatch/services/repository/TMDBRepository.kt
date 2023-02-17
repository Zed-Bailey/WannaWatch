package com.zed.wannawatch.services.repository

import com.zed.wannawatch.services.api.models.tmdb.*

interface TMDBRepository {

    suspend fun searchMovie(query: String): TMDBSearchResult<MovieResult>?

    suspend fun searchTv(query: String): TMDBSearchResult<TvResult>?

    suspend fun getMovieDetail(id: Int): MovieDetailResult?

    suspend fun getTvDetail(id: Int): TvDetailResult?

    suspend fun getTvId(id: Int): TvExternalIds?
}
