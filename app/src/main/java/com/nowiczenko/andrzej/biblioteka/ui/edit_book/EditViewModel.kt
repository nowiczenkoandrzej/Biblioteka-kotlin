package com.nowiczenko.andrzej.biblioteka.ui.edit_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenko.andrzej.biblioteka.repository.books.BooksRepository
import com.nowiczenko.andrzej.biblioteka.retrofit.UpdateBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditViewModel
@Inject constructor(
    private val booksRepository: BooksRepository
): ViewModel(){

    fun update(id: Int, book: UpdateBook){
        viewModelScope.launch(Dispatchers.IO){
            booksRepository.updateBook(id, book)
        }
    }

}