package com.zed.wannawatch.services.repository

import com.zed.wannawatch.services.api.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.api.models.tmdb.MovieResult
import com.zed.wannawatch.services.api.models.tmdb.TMDBSearchResult
import com.zed.wannawatch.services.api.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.api.models.tmdb.TvExternalIds
import com.zed.wannawatch.services.api.models.tmdb.TvResult
import com.zed.wannawatch.services.api.models.tmdb.discover.DiscoverMovies

interface TMDBRepository {

    suspend fun searchMovie(query: String): TMDBSearchResult<MovieResult>?

    suspend fun searchTv(query: String): TMDBSearchResult<TvResult>?

    suspend fun getMovieDetail(id: Int): MovieDetailResult?

    suspend fun getTvDetail(id: Int): TvDetailResult?

    suspend fun getTvId(id: Int): TvExternalIds?

    suspend fun discoverMovies(): DiscoverMovies?
}
