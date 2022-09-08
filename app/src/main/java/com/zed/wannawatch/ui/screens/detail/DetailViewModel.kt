package com.zed.wannawatch.ui.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository): ViewModel() {

    fun update(movie: Movie) = viewModelScope.launch{
        repository.updateMovie(movie)
    }

    fun delete(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }
}

class DetailViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}