package com.nowiczenko.andrzej.biblioteka.retrofit

import java.io.Serializable

data class BookItem(
    val id: Int,
    val title: String,
    val author: String,
    val cover: String,
    val publisher: String,
    val dateOfRelease: String,
    val dateOfPublishing: String,
    val amountOfPages: Int,
    val image: String,
    val user: Int
): Serializable