package com.nowiczenko.andrzej.biblioteka.ui.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenko.andrzej.biblioteka.repository.books.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel
@Inject constructor(
    private val booksRepository: BooksRepository
): ViewModel(){

    fun delete(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.deleteBook(id)
        }
    }

}