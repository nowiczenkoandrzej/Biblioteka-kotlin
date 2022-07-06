package com.nowiczenko.andrzej.biblioteka

data class BookItem(
    val amountOfPages: Int,
    val author: String,
    val cover: String,
    val dateOfPublishing: String,
    val dateOfRelease: String,
    val id: Int,
    val image: String,
    val publisher: String,
    val title: String,
    val user: Int
)