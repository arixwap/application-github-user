package com.arixwap.dicoding.aplikasigithubuser

import retrofit2.Call
import retrofit2.http.*

interface GithubApiService {
    @Headers("Authorization: token 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")
    @GET("users")
    fun getUserList(): Call<List<ListUserResponse>>

    @Headers("Authorization: token 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")
    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<SearchUserResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getUserFollower(
        @Path("username") username: String
    ): Call<List<ListUserResponse>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ListUserResponse>>
}
