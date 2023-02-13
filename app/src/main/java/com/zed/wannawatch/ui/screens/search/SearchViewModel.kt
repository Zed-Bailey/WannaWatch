package com.zed.wannawatch.ui.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.api.models.SearchDetail
import com.zed.wannawatch.services.api.models.SearchResult
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.repository.ApiRepository
import com.zed.wannawatch.services.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository): ViewModel() {

    val results = MutableLiveData<List<SearchResult>>()


    private val apiRepository = ApiRepository()

    var error by mutableStateOf(false)
        private set

    var loading by mutableStateOf(false)
        private set

    var detailResult = MutableLiveData<SearchDetail>()
    var detailLoading = MutableLiveData<Boolean>(false)

    fun search(query: String) {
        loading = true
        viewModelScope.launch {
            val result = apiRepository.searchRequest(query.trim())
            result.fold(onSuccess = { data ->
                results.value = data.results
            }, onFailure = {
                Log.e("SearchViewModel", "failed to get search results :(")
                error = true
            })
            loading = false
        }
    }

    fun getDetail(imdbId: String) {
        detailLoading.value = true
        viewModelScope.launch {
            val result = apiRepository.searchDetail(imdbId)
            result.fold(onSuccess = { data ->
                detailResult.value = data
            }, onFailure = {
                Log.e("SearchViewModel", "failed to get detail :(")
                error = true
            })

            detailLoading.value = false
        }
    }

    fun clearDetail() {

    }


    /**
     * if the error has been toggled to true, then this will toggle it off
     */
    fun toggleErrorOff() {
        if(error == true) {
            error = false
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