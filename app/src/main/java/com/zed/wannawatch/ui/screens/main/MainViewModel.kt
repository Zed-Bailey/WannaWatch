package com.zed.wannawatch.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.zed.wannawatch.services.HomeScreenWatchedFilter
import com.zed.wannawatch.services.MovieRatingFilter
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.repository.MovieRepository

// todo rename to HomeViewModel
class MainViewModel(private val repository: MovieRepository): ViewModel() {


    val movies: LiveData<List<Movie>> = repository.allMovies.asLiveData()

    val fabExpanded = mutableStateOf(true)

    fun filterMovies(movies: List<Movie>, rating: MovieRatingFilter, watchedFilter: HomeScreenWatchedFilter): List<Movie> {

        return movies.filter {

            // filter by watched status
            val result = if(watchedFilter == HomeScreenWatchedFilter.All)
                true
            else
                watchedFilter.watched == it.watched

            result
        }.filter {
            it.rating >= rating.ratingValue
        }
    }

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
