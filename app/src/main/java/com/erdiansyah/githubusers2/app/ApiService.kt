package com.erdiansyah.githubusers2.app

import com.erdiansyah.githubusers2.data.ItemsItem
import com.erdiansyah.githubusers2.data.UserData
import com.erdiansyah.githubusers2.data.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUser(
        @Query("q") login: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserData(
        @Path("username") username: String
    ): Call <UserData>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>
}
