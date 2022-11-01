package com.nowiczenko.andrzej.biblioteka.retrofit


data class UpdateBook(
    val title: String,
    val author: String,
    val cover: String,
    val publisher: String,
    val dateOfPublishing: String,
    val dateOfRelease: String,
    val amountOfPages: String,
    val user: String
)
