package com.zed.wannawatch.ui.screens.discover

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.tmdb.trending.movie.TrendingMovies
import com.zed.wannawatch.services.models.tmdb.trending.tv.TrendingTvShows
import com.zed.wannawatch.services.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val tmdbRepository: TMDBRepository
):ViewModel() {

    sealed class ErrorState {
        object NoError: ErrorState()
        data class Error(val msg: String): ErrorState()
    }

    var loading = mutableStateOf(true)
    var errorState = mutableStateOf<ErrorState>(ErrorState.NoError)
    var movieData = mutableStateOf<TrendingMovies?>(null)
    var tvShowData = mutableStateOf<TrendingTvShows?>(null)


    fun load() {
        loading.value = true
        viewModelScope.launch {
            val movies = tmdbRepository.discoverMovies()
            val tv = tmdbRepository.discoverTv()

            if(movies != null && tv != null) {
                movieData.value = movies
                tvShowData.value = tv
            } else {
                errorState.value = ErrorState.Error("An error occurred while trying to fetch the data")
            }

            loading.value = false

        }


    }

}