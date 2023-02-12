package com.zed.wannawatch.services.repository

import androidx.annotation.WorkerThread
import com.zed.wannawatch.services.database.MovieDAO
import com.zed.wannawatch.services.models.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDAO: MovieDAO) {
    val allMovies: Flow<List<Movie>> = movieDAO.getAll()

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertMovie(movie: Movie) {
        movieDAO.insertMovie(movie)
    }

    @WorkerThread
    suspend fun updateMovie(movie: Movie) {
        movieDAO.updateMovie(movie)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun getMovie(id: String): Flow<Movie> {
        return movieDAO.getMovie(id)
    }

    @WorkerThread
    suspend fun deleteMovie(movie: Movie) {
        movieDAO.deleteMovie(movie)
    }
}