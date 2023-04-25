package com.oguzdogdu.wallies.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oguzdogdu.wallies.cache.dao.FavoriteDao
import com.oguzdogdu.wallies.cache.entity.FavoriteImage
import com.oguzdogdu.wallies.cache.util.Constants.DB_NAME

@Database(version = 1, entities = [FavoriteImage::class])
abstract class FavoritesDatabase : RoomDatabase() {

    abstract val favoritesDao: FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritesDatabase? = null

        fun getInstance(context: Context): FavoritesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FavoritesDatabase::class.java, DB_NAME
        ).build()
    }
}