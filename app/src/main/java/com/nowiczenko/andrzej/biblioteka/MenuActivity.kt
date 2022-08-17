package com.nowiczenko.andrzej.biblioteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityMainBinding
import com.nowiczenko.andrzej.fragments.BooksFragment
import com.nowiczenko.andrzej.fragments.UserFragment
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_menu)

        replaceFragment(BooksFragment())

        bottom_navigation.setOnItemSelectedListener {

            when(it.itemId){
                R.id.ic_book_list -> replaceFragment(BooksFragment())
                R.id.ic_add_new_book -> addNewBook()
                R.id.ic_user_panel -> replaceFragment(UserFragment())
                else -> {
                }
            }
            true
        }

    }

    private fun addNewBook(){
        val intent = Intent(this, AddNewBookActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

    }


}


