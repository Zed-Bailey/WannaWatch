package com.zed.wannawatch.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zed.wannawatch.services.HomeScreenWatchedFilter
import com.zed.wannawatch.services.MovieRatingFilter
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.services.repository.MovieRepository

class HomeViewModel(private val repository: MovieRepository): ViewModel() {


    val movies = repository.allMovies
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
            if(rating == MovieRatingFilter.All)
                true
            else
                it.rating == rating.ratingValue
        }
    }

    fun filterResultsToType(results: List<Movie>, type: MovieType): List<Movie> {
        return results.filter {
            it.resultType == type
        }
    }

}
//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9
class HomeViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
