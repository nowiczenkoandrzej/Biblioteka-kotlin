package com.nowiczenko.andrzej.biblioteka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nowiczenko.andrzej.api.ApiInterfaceUsers
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterActivity : AppCompatActivity() {
    lateinit var users: List<UserItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        getUsers()
        setListener()
    }

    private fun createAccount(){
        if(isUserNameFree() && arePasswordsSame())
            Toast.makeText(this, "tworzenie konta", Toast.LENGTH_SHORT).show()
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
        val api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterfaceUsers::class.java)

        val retrofitUsers = api.getUser()

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


}