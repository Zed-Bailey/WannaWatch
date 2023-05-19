package com.zed.wannawatch.services.utils

import android.content.Context
import androidx.room.Room
import com.zed.wannawatch.services.database.MovieDAO
import com.zed.wannawatch.services.database.MovieDatabase
import com.zed.wannawatch.services.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideMovieDao(appDatabase: MovieDatabase): MovieDAO {
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext appContext: Context): MovieDatabase {
        return Room.databaseBuilder(
            appContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(dao: MovieDAO): MovieRepository {
        return MovieRepository(dao)
    }
}