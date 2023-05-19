package com.dicoding.githubuser.api

import com.dicoding.githubuser.data.model.DetailUserResponse
import com.dicoding.githubuser.data.model.User
import com.dicoding.githubuser.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}