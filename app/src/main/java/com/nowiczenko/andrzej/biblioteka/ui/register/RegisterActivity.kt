package com.nowiczenko.andrzej.biblioteka.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityRegisterBinding
import com.nowiczenko.andrzej.biblioteka.retrofit.UserItem
import com.nowiczenko.andrzej.biblioteka.ui.login.TAG
import com.nowiczenko.andrzej.biblioteka.utils.StateEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {


    private lateinit var users: List<UserItem>

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.internetConnection(this)

        subscribeObservers()
        setListener()

    }

    private fun subscribeObservers(){
        usersObserver()
        internetObserver()
    }

    private fun setListener() {
        binding.buttonCreateAccount.setOnClickListener {
            createAccount()
        }
        binding.buttonRefresh.setOnClickListener {
            refresh()
        }
    }

    private fun createAccount(){
        if(isUserNameFree() && arePasswordsSame()) {
            Toast.makeText(this, R.string.toast_creating_account, Toast.LENGTH_SHORT).show()
            registerRequest()
        }
    }

    private fun refresh(){
        viewModel.internetConnection(this)
    }

    private fun isUserNameFree(): Boolean {
        if(binding.editTextRegisterLogin.text.toString() != "") {
            for(user in users){
                if (user.username == binding.editTextRegisterLogin.text.toString()){
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
        return if (binding.editTextRegisterPassword.text.toString() != "" && binding.editTextRegisterRepeatPassword.text.toString() != "")
            if(binding.editTextRegisterPassword.text.toString() == binding.editTextRegisterRepeatPassword.text.toString())
                true
            else {
                Toast.makeText(this, R.string.toast_not_same_passwords, Toast.LENGTH_SHORT).show()
                false
            }
        else {
            Toast.makeText(this, R.string.toast_pick_passwords, Toast.LENGTH_SHORT).show()
            false
        }
    }


    private fun registerRequest(){
        viewModel.insert(
            UserItem(
                0,
                binding.editTextRegisterPassword.text.toString(),
                binding.editTextRegisterLogin.text.toString()
            )
        )
        finish()
    }

    private fun usersObserver(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.users.collect { event ->
                    when(event){
                        is StateEvent.Success<*> -> {
                            setVisibility(true)
                            users = event.result as List<UserItem>
                            Log.d(TAG, "onCreate: success")
                        }
                        is StateEvent.Failure -> {
                            setVisibility(true)
                            Log.d(TAG, "onCreate: ${event.error}")
                            Log.d(TAG, "onCreate: error")

                        }
                        is StateEvent.Loading -> {
                            setVisibility(false)
                            Log.d(TAG, "onCreate: loading")

                        }
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
            binding.textInputLayoutRegisterLogin.isVisible = false
            binding.textInputLayoutRegisterPassword.isVisible = false
            binding.buttonRefresh.isVisible = false
            binding.textInputLayoutRegisterRepeatPassword.isVisible = false
            binding.buttonCreateAccount.isVisible = false
            binding.textViewErrors.isVisible = false
        } else {
            binding.progressBar.isVisible = false
            binding.textInputLayoutRegisterLogin.isVisible = true
            binding.textInputLayoutRegisterPassword.isVisible = true
            binding.buttonRefresh.isVisible = false
            binding.textInputLayoutRegisterRepeatPassword.isVisible = true
            binding.buttonCreateAccount.isVisible = true
            binding.textViewErrors.isVisible = false
        }
    }

}