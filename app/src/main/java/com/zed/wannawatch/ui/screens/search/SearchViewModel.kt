package com.zed.wannawatch.ui.screens.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.zed.wannawatch.services.api.models.SearchResult
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.repository.ApiRepository
import com.zed.wannawatch.services.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository): ViewModel() {

    val results = MutableLiveData<MutableList<SearchResult>>()
    private val apiRepository = ApiRepository()
    val error = MutableLiveData<Boolean>()

    fun search(query: String) {
        viewModelScope.launch {
            val result = apiRepository.searchRequest(query.trim())
            result.fold(onSuccess = { data ->
                results.value = data.results
                error.value = true
            }, onFailure = {
                Log.e("SearchViewModel", "failed to execute network call :(")
                error.value = true
            })

        }
    }

    /**
     * if the error has been toggled to true, then this will toggle it off
     */
    fun toggleErrorOff() {
        if(error.value == true) {
            error.value = false
        }
    }

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
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