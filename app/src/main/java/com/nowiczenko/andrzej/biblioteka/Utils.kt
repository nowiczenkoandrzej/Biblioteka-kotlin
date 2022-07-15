package com.nowiczenko.andrzej.biblioteka

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.nowiczenko.andrzej.api.MyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("Range")
fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null,null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun View.snackbar(message: String){
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok"){
            snackbar.dismiss()
        }.show()
    }
}


var userNameMap = HashMap<Int, String>()
fun getUsernameById(id: String): String{

    val retrofitUsers = MyApi().getUser()

    var result = ""

    retrofitUsers.enqueue(object : Callback<List<UserItem>?> {
        override fun onResponse(
            call: Call<List<UserItem>?>,
            response: Response<List<UserItem>?>
        ) {
            val users = response.body()!!
            for(user in users){
                userNameMap.put(user.id, user.username)
            }
        }

        override fun onFailure(call: Call<List<UserItem>?>, t: Throwable) {
            println(t.message)
        }
    })

    for(user in userNameMap){
        if(user.key.toString().equals(id))
            result = user.value
    }

    return result
}