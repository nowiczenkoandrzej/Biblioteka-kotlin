package com.nowiczenko.andrzej.biblioteka


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.otherClasses.UserItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import kotlinx.coroutines.*
import retrofit2.*



const val BASE_URL = "https://biblioteka-gsoft.herokuapp.com/"

lateinit var userId: String
lateinit var userName: String

class MainActivity : AppCompatActivity() {

    private var users: List<UserItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()

        if(isOnline(this))
            getUsersRequest()
    }

    override fun onStart() {
        super.onStart()

        if (isOnline(this))
            getUsersRequest()

    }

    private fun getUsersRequest(){
        CoroutineScope(Dispatchers.IO).launch {
            users = networkCall()
        }

    }

    private suspend fun networkCall(): List<UserItem>{
        return MyApi().getUser().await()
    }

    private fun loginValidation(user: UserItem): Boolean{
        return user.username.equals(edit_text_login.text.toString()) &&
                user.password.equals(edit_text_password.text.toString())
    }

    private fun setListeners(){
        setLoginClickListener()
        setRegisterClickListener()
    }

    private fun setLoginClickListener(){
        button_login.setOnClickListener {
            if(users == null){
                Toast.makeText(this, R.string.toast_wait_for_connection, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            for(user in users!!){
                if (loginValidation(user)){
                    val intent = Intent(this, MenuActivity::class.java)
                    userId = user.id.toString()
                    userName = user.username
                    startActivity(intent)
                    return@setOnClickListener
                }
            }
            Toast.makeText(this, R.string.toast_invalid_login, Toast.LENGTH_SHORT).show()

        }
    }

    private fun setRegisterClickListener(){

        button_register.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)

        }
    }

    private fun setRefreshButtonListener() {
        button_refresh_internet_connection.setOnClickListener {
            if(isOnline(this)){
                setContentView(R.layout.activity_main)
            } else {
                Toast.makeText(this, R.string.toast_no_connection, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
