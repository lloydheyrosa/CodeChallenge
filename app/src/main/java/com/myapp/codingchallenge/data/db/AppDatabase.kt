package com.myapp.codingchallenge.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.codingchallenge.data.db.dao.ItunesItemDao
import com.myapp.codingchallenge.data.db.entities.ItunesItem

// I am using Room as the local database of my app because this is the most commonly used for MVVM android architecture,
// and it has this advantage for me that it is really easy to use, and it uses less ram and doesn't increase the apk size unlike in Realm.

/**
 * Database of *CodeChallenge*
 *
 * This class is an abstract class that serves as a Room Database
 */
@Database(
    entities = [ItunesItem::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getItemsDao(): ItunesItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()


        /**
         * Used for initializing and creating database.
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        /**
         * Builds local database
         */
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "CodeChallenge.db"
            ).build()
    }
}