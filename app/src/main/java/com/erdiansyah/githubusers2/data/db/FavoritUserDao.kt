package com.erdiansyah.githubusers2.data.db

import androidx.room.*
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritUserDao {

    @Query("SELECT * FROM user")
    fun getUser(): Flow<List<FavoritUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: FavoritUser)

    @Query("DELETE FROM user WHERE user.login = :login")
    suspend fun deleteNonFav(login: String): Int

    @Query("SELECT count(*) FROM user WHERE user.login = :login")
    suspend fun checkUser(login: String): Int
}