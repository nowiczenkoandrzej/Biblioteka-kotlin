package com.nowiczenko.andrzej.biblioteka


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nowiczenko.andrzej.api.ApiInterfaceUsers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://biblioteka-gsoft.herokuapp.com/"

class MainActivity : AppCompatActivity() {

    lateinit var users: List<UserItem>
    var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUsers()
        setButtonsListeners()

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

    private fun setButtonsListeners(){
        setLoginClickListener()
        setRegisterClickListener()
    }

    private fun setLoginClickListener(){
        button_login.setOnClickListener {
            for(user in users){
                if (loginValidation(user)){
                    Toast.makeText(this,"zalogowano", Toast.LENGTH_SHORT).show()
                    break
                }
            }
            Toast.makeText(this,"nie udalo sie zalogowac", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginValidation(user: UserItem): Boolean{
        return user.username.equals(edit_text_login.text.toString()) &&
                user.password.equals(edit_text_password.text.toString())
    }

    private fun setRegisterClickListener(){

        button_register.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

    }

}
