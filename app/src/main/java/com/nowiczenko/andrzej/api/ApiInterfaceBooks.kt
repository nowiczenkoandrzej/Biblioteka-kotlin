package com.nowiczenko.andrzej.api

import com.nowiczenko.andrzej.biblioteka.BookItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterfaceBooks {

    @GET("apibooks/")
    fun getBook(): Call<List<BookItem>>
}