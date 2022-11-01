package com.nowiczenko.andrzej.biblioteka.retrofit

import com.google.gson.annotations.SerializedName

data class PostBookItem(
    @SerializedName("title")val title: String,
    @SerializedName("author")val author: String,
    @SerializedName("cover")val cover: String,
    @SerializedName("publisher")val publisher: String,
    @SerializedName("dateOfRelease")val dateOfPublishing: String,
    @SerializedName("dateOfPublishing")val dateOfRelease: String,
    @SerializedName("amountOfPages")val amountOfPages: String,
    @SerializedName("user")val user: String
)
