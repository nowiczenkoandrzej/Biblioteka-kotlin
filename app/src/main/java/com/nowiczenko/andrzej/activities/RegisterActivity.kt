package com.nowiczenko.andrzej.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.UserItem
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    lateinit var users: List<UserItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        getUsers()
        setListener()

    }

    private fun createAccount(){
        if(isUserNameFree() && arePasswordsSame()) {
            Toast.makeText(this, "tworzenie konta", Toast.LENGTH_SHORT).show()
            register()
        } else {

        }
    }

    private fun isUserNameFree(): Boolean {
        if(edit_text_register_login.text.toString() != "") {
            for(user in users){
                if (user.username.equals(edit_text_register_login.text.toString())){
                    Toast.makeText(this, "Podany Login jest zajęty", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            return true
        } else {
            Toast.makeText(this, "Musisz podać login", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun arePasswordsSame(): Boolean {
        if (edit_text_register_password.text.toString() != "" && edit_text_register_repeat_password.text.toString() != "")
            if(edit_text_register_password.text.toString().equals(edit_text_register_repeat_password.text.toString()))
                return true
            else {
                Toast.makeText(this, "Podane hasła nie są identyczne", Toast.LENGTH_SHORT).show()
                return false
            }

        else {
            Toast.makeText(this, "Musisz wprowadzić hasła", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun setListener() {
        button_create_account.setOnClickListener {
            createAccount()
        }
    }
    private fun getUsers(){

        val retrofitUsers = MyApi().getUser()

        retrofitUsers.enqueue(object : Callback<List<UserItem>?> {
            override fun onResponse(
                call: Call<List<UserItem>?>,
                response: Response<List<UserItem>?>
            ) {
                users = response.body()!!
            }

            override fun onFailure(call: Call<List<UserItem>?>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun register(){

        val call = MyApi().pushUser(UserItem(0,edit_text_register_password.text.toString(), edit_text_register_login.text.toString()))

        call.enqueue(object : Callback<UserItem?> {
            override fun onResponse(call: Call<UserItem?>, response: Response<UserItem?>) {
                finish()
            }

            override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                println(t.message)
            }
        })
    }


}