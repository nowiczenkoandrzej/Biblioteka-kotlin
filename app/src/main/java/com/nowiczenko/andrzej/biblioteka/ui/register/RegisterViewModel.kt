package com.nowiczenko.andrzej.biblioteka.ui.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenko.andrzej.biblioteka.repository.users.UserRepository
import com.nowiczenko.andrzej.biblioteka.retrofit.UserItem
import com.nowiczenko.andrzej.biblioteka.ui.login.TAG
import com.nowiczenko.andrzej.biblioteka.utils.DataState
import com.nowiczenko.andrzej.biblioteka.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){

    private val _internetConnection = MutableStateFlow<Boolean>(false)
    val internetConnection: StateFlow<Boolean> = _internetConnection

    private val _users = MutableStateFlow<StateEvent>(StateEvent.Empty)
    val users: StateFlow<StateEvent> = _users

    fun get(){
        Log.d(TAG, "get: ")
        viewModelScope.launch(Dispatchers.IO) {
            _users.value = StateEvent.Loading
            when(val usersResponse = userRepository.getUsers()){
                is DataState.Error -> {
                    _users.value = StateEvent.Failure(usersResponse.message!!)
                }
                is DataState.Success -> {
                    if(usersResponse.data == null){
                        _users.value = StateEvent.Failure("Unexpected Error")
                    } else {
                        _users.value = StateEvent.Success(usersResponse.data)
                    }
                }
            }
        }
    }

    fun insert(user: UserItem){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
        }
    }

    fun internetConnection(context: Context){
        _internetConnection.value = userRepository.internetConnection(context)
    }

}