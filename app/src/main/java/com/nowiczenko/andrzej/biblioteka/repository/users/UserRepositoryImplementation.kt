package com.nowiczenko.andrzej.biblioteka.repository.users

import android.content.Context
import android.util.Log
import com.nowiczenko.andrzej.biblioteka.utils.isOnline
import com.nowiczenko.andrzej.biblioteka.retrofit.MyApi
import com.nowiczenko.andrzej.biblioteka.retrofit.UserItem
import com.nowiczenko.andrzej.biblioteka.utils.DataState
import javax.inject.Inject

const val TAG = "asd"

class UserRepositoryImplementation
@Inject constructor(
    private val myApi: MyApi
): UserRepository {

    override suspend fun getUsers(): DataState<List<UserItem>> {

        return try {
            val response = myApi.getUser()
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

    override suspend fun insertUser(user: UserItem) {
        try {
            myApi.pushUser(user)
        } catch (e: Exception){
            Log.d(TAG, "insertUser: ${e.message}")
        }
    }


    override fun internetConnection(context: Context): Boolean{
        return isOnline(context)
    }
}