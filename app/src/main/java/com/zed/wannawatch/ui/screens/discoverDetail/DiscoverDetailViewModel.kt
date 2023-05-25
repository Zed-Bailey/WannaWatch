package com.zed.wannawatch.ui.screens.discoverDetail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.movie.Movie
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.repository.MovieRepository
import com.zed.wannawatch.services.repository.TMDBRepository
import com.zed.wannawatch.ui.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tmdbRepository: TMDBRepository,
    private val repository: MovieRepository,
) : ViewModel() {

    val tmdbId = savedStateHandle.get<Int>("tmdbId")
    val type = savedStateHandle.get<MovieType>("type")

    var seriesDetail = MutableLiveData<TvDetailResult>()
    var movieDetail = MutableLiveData<MovieDetailResult>()

    var loading = mutableStateOf(true)

    var error = mutableStateOf<ErrorState>(ErrorState.NoError)

    /**
     * Gets the detail model for the movie or tv show from the api
     */
    fun getDetail() {
        loading.value = true
        viewModelScope.launch{
            when(type!!) {
                MovieType.Movie -> {

                    val response = tmdbRepository.getMovieDetail(tmdbId!!)
                    if(response != null) {
                        movieDetail.value = response
                    } else {
                        // set error
                    }
                    loading.value = false
                }

                MovieType.Series -> {
                    val response = tmdbRepository.getTvDetail(tmdbId!!)
                    if(response != null) {
                        seriesDetail.value = response
                    } else {
                        // set error
                    }
                    loading.value = false

                }

            }


        }
    }

    /**
     * adds the movie model to the database
     * if the movie is a series then it will also query the api and fetch the imdb id
     */
    fun addModel(movie: Movie) = viewModelScope.launch {
        var model = movie

        // if is a series fetch the imdb id from the api
        if(movie.resultType == MovieType.Series) {
            val response = tmdbRepository.getTvId(movie.tmdbId)
            Log.d("com.zed.wannawatch", "$response")
            if(response?.imdb_id != null) {
                model = movie.copy(
                    imdbID = response.imdb_id
                )
            } else {
                error.value = ErrorState.Error("Error getting imdb id")
            }

        }

        if(error.value is ErrorState.NoError) {
            repository.insertMovie(model)
        }
    }

}