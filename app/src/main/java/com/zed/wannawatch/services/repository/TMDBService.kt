package com.zed.wannawatch.services.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zed.wannawatch.services.api.models.tmdb.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TMDBService {
    @GET("/3/search/movie")
    suspend fun searchMovies(@Query("api_key") key: String, @Query("query") query: String): Response<TMDBSearchResult<MovieResult>>

    @GET("/3/search/tv")
    suspend fun searchTvShows(@Query("api_key") key: String, @Query("query") query: String): Response<TMDBSearchResult<TvResult>>

    @GET("/3/movie/{id}")
    suspend fun getMovieDetail(@Path("id") tmdbId: Int, @Query("api_key") key: String,): Response<MovieDetailResult>

    @GET("/3/tv/{id}")
    suspend fun getTvDetail(@Path("id") tmdbId: Int, @Query("api_key") key: String): Response<TvDetailResult>


    @GET("/3/tv/{tmdbId}/external_ids")
    suspend fun getTvIds(@Path("tmdbId") tmdbId: Int, @Query("api_key") key: String): Response<TvExternalIds>
}

object TMDBServiceHelper {

    private val jsonProperties = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        this.encodeDefaults = true

    }

    @OptIn(ExperimentalSerializationApi::class)
    fun getInstance(): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            // use kotlinx.serialization as converter factory
            .addConverterFactory(jsonProperties.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}