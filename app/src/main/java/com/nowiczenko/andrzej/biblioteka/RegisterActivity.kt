package com.nowiczenko.andrzej.biblioteka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.otherClasses.UserItem
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await


class RegisterActivity : AppCompatActivity() {
    lateinit var users: List<UserItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        getUsersRequest()
        setListener()

    }

    private fun createAccount(){
        if(isUserNameFree() && arePasswordsSame()) {
            Toast.makeText(this, R.string.toast_creating_account, Toast.LENGTH_SHORT).show()
            registerRequest()
        }
    }

    private fun isUserNameFree(): Boolean {
        if(edit_text_register_login.text.toString() != "") {
            for(user in users){
                if (user.username.equals(edit_text_register_login.text.toString())){
                    Toast.makeText(this, R.string.toast_login_taken, Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            return true
        } else {
            Toast.makeText(this, R.string.toast_pick_login, Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun arePasswordsSame(): Boolean {
        if (edit_text_register_password.text.toString() != "" && edit_text_register_repeat_password.text.toString() != "")
            if(edit_text_register_password.text.toString().equals(edit_text_register_repeat_password.text.toString()))
                return true
            else {
                Toast.makeText(this, R.string.toast_not_same_passwords, Toast.LENGTH_SHORT).show()
                return false
            }

        else {
            Toast.makeText(this, R.string.toast_pick_passwords, Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun setListener() {
        button_create_account.setOnClickListener {
            createAccount()
        }
    }


    private fun getUsersRequest(){
        CoroutineScope(Dispatchers.IO).launch {
            users = MyApi().getUser().await()
        }
    }

    private fun registerRequest(){
        CoroutineScope(Dispatchers.IO).launch {
            MyApi()
                .pushUser(
                    UserItem(
                    0,
                    edit_text_register_password.text.toString()
                    , edit_text_register_login.text.toString())
                )
                .await()
        }
        finish()
    }

}