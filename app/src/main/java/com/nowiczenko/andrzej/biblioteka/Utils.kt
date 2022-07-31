package com.nowiczenko.andrzej.biblioteka

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
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

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}