package com.zed.wannawatch.services

import android.app.Application
import com.zed.wannawatch.services.database.MovieDatabase
import com.zed.wannawatch.services.repository.MovieRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication: Application() {
    val database by lazy { MovieDatabase.getDatabase(this) }
    val repository by lazy { MovieRepository(database.movieDao()) }
}