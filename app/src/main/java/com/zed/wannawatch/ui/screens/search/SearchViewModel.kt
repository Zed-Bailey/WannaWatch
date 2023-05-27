package com.zed.wannawatch.ui.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tmdbRepository: TMDBRepository
): ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Nothing)
    val state = _state.asStateFlow()


    var selected = mutableStateOf(MovieType.Movie)

    var lastSearchString = ""


    fun setSelectedType(type: MovieType) {
        selected.value = type
    }



    fun search(query: String, type: MovieType) {
        lastSearchString = query

        viewModelScope.launch{
            _state.value = SearchState.Loading

            when(type) {
                MovieType.Movie -> {

                    val response = tmdbRepository.searchMovie(query)

                    if (response == null) {
                        _state.value = SearchState.Error("Sorry there was an error while searching")
                    } else {
                        _state.value = SearchState.MovieSearchSuccess(response)
                    }
                }

                MovieType.Series -> {
                    val response = tmdbRepository.searchTv(query)
                    if (response == null) {
                        _state.value = SearchState.Error("Sorry there was an error while searching")
                    } else {
                        _state.value = SearchState.SeriesSearchSuccess(response)
                    }
                }
            }
        }

    }

    /**
     * Retry's the last searched query
     */
    fun retry() {
        search(lastSearchString, selected.value)
    }
}