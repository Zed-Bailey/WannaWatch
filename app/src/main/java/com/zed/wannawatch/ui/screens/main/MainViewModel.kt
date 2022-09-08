package com.zed.wannawatch.ui.screens.main

import androidx.lifecycle.*
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.repository.MovieRepository

class MainViewModel(private val repository: MovieRepository): ViewModel() {

    val movies: LiveData<List<Movie>> = repository.allMovies.asLiveData()

}
//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9
class MainViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
