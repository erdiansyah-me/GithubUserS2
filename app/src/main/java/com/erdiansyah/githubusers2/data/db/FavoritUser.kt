package com.erdiansyah.githubusers2.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class FavoritUser(
    @ColumnInfo(name = "login")
    @PrimaryKey
    var login: String,

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String
) : Serializable
