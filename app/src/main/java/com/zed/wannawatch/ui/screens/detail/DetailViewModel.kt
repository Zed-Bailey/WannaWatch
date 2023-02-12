package com.zed.wannawatch.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.services.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository, private val movieId: String): ViewModel() {
//    private val movieModel: Movie

    private val _movieState = MutableStateFlow(Movie("","","", resultType = MovieType.Movie))
    val movieState = _movieState.asStateFlow()


    init {
        getMovie(movieId)
    }

    fun toggleWatched() {
        _movieState.update {
            it.copy(
                watched = !it.watched
            )
        }
        // updates value in DB
        update(_movieState.value)
    }

    fun updateNotesText(text: String) {
        _movieState.update {
            it.copy(
                notes = text
            )
        }
        update(_movieState.value)
    }

    fun updateRating(ratingValue: Int) {
        _movieState.update {
            it.copy(
                rating = ratingValue
            )
        }
        update(_movieState.value)
    }

    private fun getMovie(id: String) = viewModelScope.launch {
        val movie = repository.getMovie(id)
        movie.collect {flow ->
            _movieState.update {
                flow
            }
        }
    }

    private fun update(movie: Movie) = viewModelScope.launch{
        repository.updateMovie(movie)
    }

    fun delete(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }
}

class DetailViewModelFactory(private val repository: MovieRepository, private val movieId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository, movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}