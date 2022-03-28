package com.erdiansyah.githubusers2.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query

@Dao
interface FavoritUserDao {

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<List<FavoritUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: FavoritUser)

    @Query("DELETE FROM user WHERE user.login = :login")
    suspend fun deleteNonFav(login: String): Int

    @Query("SELECT count(*) FROM user WHERE user.login = :login")
    suspend fun checkUser(login: String): Int
}