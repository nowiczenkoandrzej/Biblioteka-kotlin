package com.nowiczenko.andrzej.biblioteka.ui.add_new_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenko.andrzej.biblioteka.repository.books.BooksRepository
import com.nowiczenko.andrzej.biblioteka.retrofit.PostBookItem
import com.nowiczenko.andrzej.biblioteka.retrofit.UploadRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddNewBookViewModel
@Inject constructor(
    private val booksRepository: BooksRepository
): ViewModel(){

    fun upload(body: UploadRequestBody, name: String, book: PostBookItem){
        viewModelScope.launch(Dispatchers.IO){
            booksRepository.postBook(body, name, book)
        }
    }

}