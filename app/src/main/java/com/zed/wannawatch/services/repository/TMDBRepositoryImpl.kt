package com.zed.wannawatch.services.repository

import com.zed.wannawatch.BuildConfig
import com.zed.wannawatch.services.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.models.tmdb.MovieResult
import com.zed.wannawatch.services.models.tmdb.TMDBSearchResult
import com.zed.wannawatch.services.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.models.tmdb.TvExternalIds
import com.zed.wannawatch.services.models.tmdb.TvResult
import com.zed.wannawatch.services.models.tmdb.discover.DiscoverMovies


class TMDBRepositoryImpl(
    private val service: TMDBService = TMDBServiceHelper.getInstance().create(TMDBService::class.java)
): TMDBRepository {

    companion object {
        const val tmdb_key = BuildConfig.TMDBApiKey
    }

    override suspend fun searchMovie(query: String): TMDBSearchResult<MovieResult>? {
        val response = service.searchMovies(tmdb_key, query)

        if(response.isSuccessful) {
            return response.body()
        }

        return null
    }

    override suspend fun searchTv(query: String): TMDBSearchResult<TvResult>? {
        val response = service.searchTvShows(tmdb_key, query)

        if(response.isSuccessful) {
            return response.body()
        }

        return null
    }

    override suspend fun getMovieDetail(id: Int): MovieDetailResult? {
        val response = service.getMovieDetail(id, tmdb_key)

        if(response.isSuccessful) {
            return response.body()
        }

        return null
    }

    override suspend fun getTvDetail(id: Int): TvDetailResult? {
        val response = service.getTvDetail(id, tmdb_key)

        if(response.isSuccessful) {
            return response.body()
        }

        return null
    }

    override suspend fun getTvId(id: Int): TvExternalIds? {
        // uses the tmdb id of the tv show to get the imdb id
        // https://developers.themoviedb.org/3/tv/get-tv-external-ids
        val response = service.getTvIds(id, tmdb_key)
        if(response.isSuccessful) {
            return response.body()
        }

        return null
    }

    override suspend fun discoverMovies(): DiscoverMovies? {
        val response = service.getDiscoverMovies(tmdb_key)
        if(response.isSuccessful) {
            return response.body()
        }

        return null
    }


}