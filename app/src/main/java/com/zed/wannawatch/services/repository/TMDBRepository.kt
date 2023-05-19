package com.zed.wannawatch.services.repository

import com.zed.wannawatch.services.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.models.tmdb.MovieResult
import com.zed.wannawatch.services.models.tmdb.TMDBSearchResult
import com.zed.wannawatch.services.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.models.tmdb.TvExternalIds
import com.zed.wannawatch.services.models.tmdb.TvResult
import com.zed.wannawatch.services.models.tmdb.trending.movie.TrendingMovies
import com.zed.wannawatch.services.models.tmdb.trending.tv.TrendingTvShows

interface TMDBRepository {

    suspend fun searchMovie(query: String): TMDBSearchResult<MovieResult>?

    suspend fun searchTv(query: String): TMDBSearchResult<TvResult>?

    suspend fun getMovieDetail(id: Int): MovieDetailResult?

    suspend fun getTvDetail(id: Int): TvDetailResult?

    suspend fun getTvId(id: Int): TvExternalIds?

    suspend fun discoverMovies(): TrendingMovies?

    suspend fun discoverTv(): TrendingTvShows?

}
