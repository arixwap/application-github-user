package com.arixwap.dicoding.aplikasigithubuser

import retrofit2.Call
import retrofit2.http.*

interface GithubApiService {
    @GET("users")
    fun getUserList(): Call<List<UserResponse>>

    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    fun getUserFollower(
        @Path("username") username: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserResponse>>
}
