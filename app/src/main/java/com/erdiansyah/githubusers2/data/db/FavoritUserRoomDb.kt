package com.erdiansyah.githubusers2.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritUser::class], version = 1, exportSchema = false)
abstract class FavoritUserRoomDb : RoomDatabase() {
    abstract fun favoritUserDao(): FavoritUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritUserRoomDb? = null
        fun getInstance(context: Context): FavoritUserRoomDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoritUserRoomDb::class.java,"user.db"
                ).build()
            }
    }
}