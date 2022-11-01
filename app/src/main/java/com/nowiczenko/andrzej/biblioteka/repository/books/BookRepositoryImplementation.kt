package com.nowiczenko.andrzej.biblioteka.repository.books

import android.content.Context
import com.nowiczenko.andrzej.biblioteka.utils.isOnline
import com.nowiczenko.andrzej.biblioteka.retrofit.*
import com.nowiczenko.andrzej.biblioteka.ui.login.userId
import com.nowiczenko.andrzej.biblioteka.utils.DataState
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class BookRepositoryImplementation
@Inject constructor(
    private val myApi: MyApi
): BooksRepository {

    override suspend fun getBooks(): DataState<List<BookItem>> {

        return try {
            val response = myApi.getBook()
            val result = response.body()
            if(response.isSuccessful && result != null){
                DataState.Success(result)
            } else {
                DataState.Error(response.message())
            }
        } catch (e: Exception){
            DataState.Error(e.message?: "An error occured")
        }
    }

    override suspend fun postBook(
        body: UploadRequestBody,
        name: String,
        book: PostBookItem
    ): Boolean {
        return try {
            myApi.postBook(
                title = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.title
                ),
                author = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.author
                ),
                cover = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.cover
                ),
                publisher = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.publisher
                ),
                dateOfPublishing = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.dateOfPublishing
                ),
                dateOfRelease = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.dateOfRelease
                ),
                amountOfPages = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    book.amountOfPages
                ),
                image = MultipartBody.Part
                    .createFormData(
                        "image",
                        name,
                        body
                    ),
                user = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    userId
                )
            )
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }

    }

    override suspend fun updateBook(id: Int, book: UpdateBook) {
        myApi.updateBook(id, book)
    }

    override suspend fun deleteBook(id: Int) {
        myApi.deleteBook(id)
    }

    override fun internetConnection(context: Context): Boolean {
        return isOnline(context)
    }
}