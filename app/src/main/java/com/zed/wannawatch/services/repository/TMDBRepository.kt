package com.zed.wannawatch.services.repository

import com.zed.wannawatch.services.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.models.tmdb.MovieResult
import com.zed.wannawatch.services.models.tmdb.TMDBSearchResult
import com.zed.wannawatch.services.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.models.tmdb.TvExternalIds
import com.zed.wannawatch.services.models.tmdb.TvResult
import com.zed.wannawatch.services.models.tmdb.findById.FindByIdResults
import com.zed.wannawatch.services.models.tmdb.trending.movie.TrendingMovies
import com.zed.wannawatch.services.models.tmdb.trending.tv.TrendingTvShows
import com.zed.wannawatch.services.models.tmdb.videos.MovieTrailer

interface TMDBRepository {

    suspend fun findTmdbIdFromImdbId(imdbId: String) : FindByIdResults?
    suspend fun searchMovie(query: String): TMDBSearchResult<MovieResult>?

    suspend fun searchTv(query: String): TMDBSearchResult<TvResult>?

    suspend fun getMovieDetail(id: Int): MovieDetailResult?

    suspend fun getTvDetail(id: Int): TvDetailResult?

    suspend fun getTvId(id: Int): TvExternalIds?

    suspend fun discoverMovies(): TrendingMovies?

    suspend fun discoverTv(): TrendingTvShows?

    suspend fun getMovieTrailers(movieId: Int): List<MovieTrailer>?
}
