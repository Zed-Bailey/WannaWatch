package com.zed.wannawatch.ui.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.api.models.*
import com.zed.wannawatch.services.api.models.tmdb.*
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.services.repository.*
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository): ViewModel() {

    val movieSearchResults = MutableLiveData<TMDBSearchResult<MovieResult>>()
    val seriesSearchResults = MutableLiveData<TMDBSearchResult<TvResult>>()


    var error by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var loading by mutableStateOf(false)
        private set

    var seriesDetail = MutableLiveData<TvDetailResult>()
    var movieDetail = MutableLiveData<MovieDetailResult>()

    var detailLoading = MutableLiveData(false)

    private val tmdbRepository = TMDBRepositoryImpl()


    fun search(query: String, type: MovieType) {
        loading = true

        viewModelScope.launch{
            when(type) {
                MovieType.Movie -> {

                    val response = tmdbRepository.searchMovie(query)
                    if(response != null) {
                        movieSearchResults.value = response
                    } else {
                        // set error
                    }

                    loading = false
                }

                MovieType.Series -> {
                    val response = tmdbRepository.searchTv(query)
                    if(response != null) {
                        seriesSearchResults.value = response
                    } else {
                        // set error
                    }

                    loading = false
                }
            }
        }

    }

    /**
     * Gets the detail model for the movie or tv show from the api
     */
    fun getDetail(tmdbId: Int, type: MovieType) {
        detailLoading.value = true
        viewModelScope.launch{
            when(type) {
                MovieType.Movie -> {

                    val response = tmdbRepository.getMovieDetail(tmdbId)
                    if(response != null) {
                        movieDetail.value = response
                    } else {
                        // set error
                    }
                    detailLoading.value = false

                }

                MovieType.Series -> {
                    val response = tmdbRepository.getTvDetail(tmdbId)
                    if(response != null) {
                        seriesDetail.value = response
                    } else {
                        // set error
                    }

                    detailLoading.value = false
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
                error = true
                errorMessage = "Failed to get IMDB id"
            }

        }

        if(!error) {
            repository.insertMovie(model)
        }
    }
}

//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9
class SearchViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}