package com.zed.wannawatch.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val tmdbRepository: TMDBRepository
):ViewModel() {

    private val _state = MutableStateFlow<DiscoverState>(DiscoverState.Loading)
    val state = _state.asStateFlow()


    fun load() {

        viewModelScope.launch {
            val movies = tmdbRepository.discoverMovies()
            val tv = tmdbRepository.discoverTv()

            if(movies != null && tv != null) {
                _state.value = DiscoverState.Success(movies, tv)
            } else {
                _state.value = DiscoverState.Error("An error occurred while trying to fetch the data")

            }

        }


    }

}