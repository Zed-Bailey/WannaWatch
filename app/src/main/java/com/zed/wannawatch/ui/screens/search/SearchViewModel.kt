package com.zed.wannawatch.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.models.tmdb.MovieResult
import com.zed.wannawatch.services.models.tmdb.TMDBSearchResult
import com.zed.wannawatch.services.models.tmdb.TvResult
import com.zed.wannawatch.services.repository.MovieRepository
import com.zed.wannawatch.services.repository.TMDBRepository
import com.zed.wannawatch.ui.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val tmdbRepository: TMDBRepository
): ViewModel() {

    val movieSearchResults = MutableLiveData<TMDBSearchResult<MovieResult>>()
    val seriesSearchResults = MutableLiveData<TMDBSearchResult<TvResult>>()


    var errorState by mutableStateOf<ErrorState>(ErrorState.NoError)

    var loading by mutableStateOf(false)
        private set

    var selected = mutableStateOf(MovieType.Movie)

    fun setSelectedType(type: MovieType) {
        selected.value = type
    }



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
                        errorState = ErrorState.Error("Error loading movie search results")
                    }

                    loading = false
                }

                MovieType.Series -> {
                    val response = tmdbRepository.searchTv(query)
                    if(response != null) {
                        seriesSearchResults.value = response
                    } else {
                        // set error
                        errorState = ErrorState.Error("Error loading series search results")
                    }

                    loading = false
                }
            }
        }

    }

    fun retry(query: String, type: MovieType) {

        errorState = ErrorState.NoError

        search(query, type)
    }
}