package com.zed.wannawatch.services.repository

import androidx.annotation.WorkerThread
import com.zed.wannawatch.services.database.MovieDAO
import com.zed.wannawatch.services.models.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieDAO: MovieDAO) {


    @WorkerThread
    suspend fun insertMovie(movie: Movie) {
        movieDAO.insertMovie(movie)
    }

    @WorkerThread
    suspend fun getMovies(): Flow<List<Movie>> {
        return movieDAO.getAll()
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