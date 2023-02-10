package com.zed.wannawatch.services.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zed.wannawatch.services.models.Movie

@Database(
    version = 2,
    entities = [Movie::class],
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDAO

    //    https://developer.android.com/codelabs/android-room-with-a-view-kotlin#7
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MovieDatabase? = null


        fun getDatabase(context: Context): MovieDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}