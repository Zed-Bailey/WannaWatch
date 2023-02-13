package com.zed.wannawatch.services.database

import androidx.room.*
import com.zed.wannawatch.services.models.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO  {

    @Query("SELECT * FROM Movie ORDER BY title ASC")
    fun getAll(): Flow<List<Movie>>

    @Query("Select * FROM Movie WHERE imdbID = :id")
    fun getMovie(id: String): Flow<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend  fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)
}