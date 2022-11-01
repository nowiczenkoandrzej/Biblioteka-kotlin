package com.nowiczenko.andrzej.biblioteka.repository.users

import android.content.Context
import com.nowiczenko.andrzej.biblioteka.retrofit.UserItem
import com.nowiczenko.andrzej.biblioteka.utils.DataState

interface UserRepository {
    suspend fun getUsers(): DataState<List<UserItem>>
    suspend fun insertUser(user: UserItem)
    fun internetConnection(context: Context): Boolean
}