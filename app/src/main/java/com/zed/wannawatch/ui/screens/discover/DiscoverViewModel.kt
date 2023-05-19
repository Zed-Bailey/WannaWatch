package com.zed.wannawatch.ui.screens.discover

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.tmdb.discover.DiscoverMovies
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
    var data = mutableStateOf<DiscoverMovies?>(null)



    fun load() {
        loading.value = true
        viewModelScope.launch {
            val movies = tmdbRepository.discoverMovies()
            if(movies != null) {
                data.value = movies
            } else {
                errorState.value = ErrorState.Error("An error occurred while trying to fetch the data")
            }

            loading.value = false

        }


    }

}