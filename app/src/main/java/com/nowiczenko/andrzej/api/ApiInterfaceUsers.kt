package com.nowiczenko.andrzej.api

import com.nowiczenko.andrzej.biblioteka.UserItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterfaceUsers {

    @GET("apiusers/")
    fun getUser(): Call<List<UserItem>>

    @POST("adduser/")
    fun pushUser(@Body user: UserItem): Call<UserItem>
}