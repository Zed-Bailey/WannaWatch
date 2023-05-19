package com.zed.wannawatch.services.utils

import com.zed.wannawatch.services.repository.TMDBRepository
import com.zed.wannawatch.services.repository.TMDBRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TMDBModule {

    @Provides
    @Singleton
    fun provideTMDBRepository(): TMDBRepository {
        return TMDBRepositoryImpl()
    }

}

