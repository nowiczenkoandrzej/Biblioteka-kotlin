package com.nowiczenko.andrzej.biblioteka.ui.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityLoginBinding
import com.nowiczenko.andrzej.biblioteka.retrofit.UserItem
import com.nowiczenko.andrzej.biblioteka.ui.menu.MenuActivity
import com.nowiczenko.andrzej.biblioteka.ui.register.RegisterActivity
import com.nowiczenko.andrzej.biblioteka.utils.StateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


lateinit var userId: String
lateinit var userName: String

const val TAG = "asd"

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private var users: List<UserItem>? = null

    private val viewModel: LoginViewModel by viewModels()


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.internetConnection(this)

        setListeners()
        subscribeObservers()

    }

    private fun setListeners(){
        setLoginClickListener()
        setRegisterClickListener()
        setRefreshButtonListener()
    }

    private fun subscribeObservers(){
        usersObserver()
        internetObserver()
    }

    private fun usersObserver(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.users.collect { event ->
                    when(event){
                        is StateEvent.Success<*> -> {
                            setVisibility(true)
                            users = event.result as List<UserItem>?
                        }
                        is StateEvent.Failure -> setVisibility(true)
                        is StateEvent.Loading -> setVisibility(false)
                        is StateEvent.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun internetObserver(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.internetConnection.collect { connection ->
                    if(connection){
                        viewModel.get()
                        binding.progressBar.isVisible = false
                    } else {
                        setVisibility(false)
                        binding.progressBar.isVisible = false
                        binding.buttonRefresh.isVisible = true
                        binding.textViewErrors.isVisible = true
                        binding.textViewErrors.text = "Brak połączenia z internetem"


                    }
                }
            }
        }
    }


    private fun setVisibility(loading: Boolean){
        if(!loading){
            binding.progressBar.isVisible = true
            binding.buttonLogin.isVisible = false
            binding.buttonRegister.isVisible = false
            binding.buttonRefresh.isVisible = false
            binding.textInputLayoutLogin.isVisible = false
            binding.textInputLayoutPassword.isVisible = false
            binding.textViewErrors.isVisible = false
        } else {
            binding.progressBar.isVisible = false
            binding.buttonRefresh.isVisible = false
            binding.buttonLogin.isVisible = true
            binding.buttonRegister.isVisible = true
            binding.textInputLayoutLogin.isVisible = true
            binding.textInputLayoutPassword.isVisible = true
            binding.textViewErrors.isVisible = false
        }
    }

    private fun loginValidation(user: UserItem): Boolean{
        return user.username == binding.editTextLogin.text.toString() &&
                user.password == binding.editTextPassword.text.toString()
    }

    private fun setLoginClickListener(){
        binding.buttonLogin.setOnClickListener {
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

        binding.buttonRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)

        }
    }

    private fun setRefreshButtonListener(){
        binding.buttonRefresh.setOnClickListener {
            viewModel.internetConnection(this)
            binding.progressBar.isVisible = true


             lifecycleScope.launch() {
                 val slideIn = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in)
                 val slideOut = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out)

                 binding.progressBar.startAnimation(slideIn)
                 binding.progressBar.isVisible = true
                 delay(750L)

                 binding.progressBar.startAnimation(slideOut)
                 delay(250L)
                 binding.progressBar.isVisible = false

             }

/*            runBlocking {
                Log.d(TAG, "setRefreshButtonListener: runblocking")
                binding.progressBar.isVisible = true
                delay(1000L)
                binding.progressBar.isVisible = false
                Log.d(TAG, "setRefreshButtonListener: runblocking")

            }*/
        }
    }

}
