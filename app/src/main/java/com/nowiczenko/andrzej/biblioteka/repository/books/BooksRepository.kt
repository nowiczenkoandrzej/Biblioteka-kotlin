package com.nowiczenko.andrzej.biblioteka.repository.books

import android.content.Context
import com.nowiczenko.andrzej.biblioteka.retrofit.BookItem
import com.nowiczenko.andrzej.biblioteka.retrofit.PostBookItem
import com.nowiczenko.andrzej.biblioteka.retrofit.UpdateBook
import com.nowiczenko.andrzej.biblioteka.retrofit.UploadRequestBody
import com.nowiczenko.andrzej.biblioteka.utils.DataState


interface BooksRepository {
    suspend fun getBooks(): DataState<List<BookItem>>
    suspend fun postBook(body: UploadRequestBody,name: String, book: PostBookItem): Boolean
    suspend fun updateBook(id: Int, book: UpdateBook)
    suspend fun deleteBook(id: Int)
    fun internetConnection(context: Context): Boolean
}