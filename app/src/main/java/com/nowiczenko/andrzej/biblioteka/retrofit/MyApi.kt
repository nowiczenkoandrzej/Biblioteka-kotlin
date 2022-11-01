package com.nowiczenko.andrzej.biblioteka.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.*

interface MyApi {

    @GET("apiusers/")
    suspend fun getUser(): Response<List<UserItem>>

    @POST("adduser/")
    suspend fun pushUser(@Body user: UserItem): Response<UserItem>

    @GET("apibooks/")
    suspend fun getBook(): Response<List<BookItem>>

    @Multipart
    @POST("postbook/")
    suspend fun postBook(
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("cover") cover: RequestBody,
        @Part("publisher") publisher: RequestBody,
        @Part("dateOfPublishing") dateOfPublishing: RequestBody,
        @Part("dateOfRelease") dateOfRelease: RequestBody,
        @Part("amountOfPages") amountOfPages: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody
    )


    @DELETE("bookdetail/{id}/")
    suspend fun deleteBook(
        @Path("id")id : Int
    ): Response<Void>


    @PUT("bookdetail/{id}/")
    suspend fun updateBook(
        @Path("id")id : Int,
        @Body book: UpdateBook
    ): Response<UpdateBook>

    companion object{
        operator fun invoke(): MyApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://biblioteka-gsoft.herokuapp.com/")
                .build()
                .create(MyApi::class.java)
        }

    }
}