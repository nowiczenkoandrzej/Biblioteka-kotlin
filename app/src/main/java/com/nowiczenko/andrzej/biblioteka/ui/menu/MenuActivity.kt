package com.nowiczenko.andrzej.biblioteka.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityMenuBinding
import com.nowiczenko.andrzej.biblioteka.ui.add_new_book.AddNewBookActivity
import com.nowiczenko.andrzej.biblioteka.ui.books_screen.BooksFragment
import com.nowiczenko.andrzej.biblioteka.ui.user_screen.UserFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : FragmentActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(BooksFragment())

        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){
                R.id.ic_book_list -> replaceFragment(BooksFragment())
                R.id.ic_user_panel -> replaceFragment(UserFragment())
                R.id.ic_add_new_book -> addNewBook()
                else -> {
                }
            }
            true
        }

    }

    override fun onBackPressed() {
        showDialog()
    }

    private fun showDialog(){
        val dialog = AlertDialog.Builder(this)

        dialog.setMessage("Czy chcesz się wylogować?")

        dialog.setPositiveButton("Tak") { _, _ ->
            finish()
        }

        dialog.setNegativeButton("Nie") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
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


