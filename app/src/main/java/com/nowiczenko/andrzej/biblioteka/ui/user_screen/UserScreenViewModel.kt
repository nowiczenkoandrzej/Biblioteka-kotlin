package com.nowiczenko.andrzej.biblioteka.ui.user_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenko.andrzej.biblioteka.repository.books.BooksRepository
import com.nowiczenko.andrzej.biblioteka.utils.DataState
import com.nowiczenko.andrzej.biblioteka.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserScreenViewModel
@Inject constructor(
    private val booksRepository: BooksRepository
): ViewModel() {

    private val _internetConnection = MutableStateFlow<Boolean>(false)
    val internetConnection: StateFlow<Boolean> = _internetConnection

    private val _books = MutableStateFlow<StateEvent>(StateEvent.Empty)
    val books: StateFlow<StateEvent> = _books

    fun get(){
        viewModelScope.launch(Dispatchers.IO) {
            _books.value = StateEvent.Loading
            when(val booksResponse = booksRepository.getBooks()){
                is DataState.Error -> {
                    _books.value = StateEvent.Failure(booksResponse.message!!)
                }
                is DataState.Success -> {
                    if(booksResponse.data == null){
                        _books.value = StateEvent.Failure("Unexpected Error")
                    } else {
                        _books.value = StateEvent.Success(booksResponse.data)
                    }
                }
            }
        }
    }

    fun internetConnection(context: Context){
        _internetConnection.value = booksRepository.internetConnection(context)
    }

}