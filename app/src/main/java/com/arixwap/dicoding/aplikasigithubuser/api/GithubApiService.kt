package com.arixwap.dicoding.aplikasigithubuser.api

import com.arixwap.dicoding.aplikasigithubuser.database.SearchUserResponse
import com.arixwap.dicoding.aplikasigithubuser.database.User
import retrofit2.Call
import retrofit2.http.*

interface GithubApiService {
    @GET("users")
    fun getUserList(): Call<List<User>>

    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}/followers")
    fun getUserFollower(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>
}
