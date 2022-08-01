package com.nowiczenko.andrzej.api

import com.nowiczenko.andrzej.biblioteka.BASE_URL
import com.nowiczenko.andrzej.otherClasses.BookItem
import com.nowiczenko.andrzej.otherClasses.UserItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyApi {

    @GET("apibooks/")
    fun getBook(): Call<List<BookItem>>

    @Multipart
    @POST("postbook/")
    fun postBook(
        @Part("title")title : RequestBody,
        @Part("author")author : RequestBody,
        @Part("cover")cover : RequestBody,
        @Part("publisher")publisher : RequestBody,
        @Part("dateOfPublishing")dateOfPublishing : RequestBody,
        @Part("dateOfRelease")dateOfRelease : RequestBody,
        @Part("amountOfPages")amountOfPages : RequestBody,
        @Part image: MultipartBody.Part,
        @Part("user")user: RequestBody
    ): Call<PostBookItem>

    @GET("apiusers/")
    fun getUser(): Call<List<UserItem>>

    @POST("adduser/")
    fun pushUser(@Body user: UserItem): Call<UserItem>


    @PUT("bookdetail/{id}/")
    fun updateBook(
        @Path("id")id : Int,
        @Body book: UpdateBook
    ): Call<UpdateBook>

    companion object{
        operator fun invoke(): MyApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(MyApi::class.java)
        }

    }
}