package com.nowiczenko.andrzej.biblioteka.ui.edit_book

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.retrofit.UpdateBook
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityEditBinding
import com.nowiczenko.andrzej.biblioteka.retrofit.BookItem
import com.nowiczenko.andrzej.biblioteka.ui.login.userId
import com.nowiczenko.andrzej.biblioteka.ui.menu.MenuActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*


@AndroidEntryPoint
class EditActivity : AppCompatActivity() {


    private lateinit var book: BookItem

    private lateinit var binding: ActivityEditBinding

    //Calendar
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private val viewModel: EditViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getSerializableExtra("book") as BookItem

        setListeners()
        setContent(book)
        setSpinnerAdapter()
    }

    private fun setListeners(){

        binding.buttonPublishEdit.setOnClickListener {
            uploadBook()
        }

        binding.textViewDateOfReleaseEdit.setOnClickListener {
            pickDate()
        }
    }

    private fun setSpinnerAdapter(){
        val values = arrayOf("Miękka", "Twarda")
        binding.spinnerCoverTypeEdit.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, values)
    }

    private fun setContent(book: BookItem){
        Picasso.get().load(book.image).into(binding.imageViewEditBook)
        binding.editTextBooksTitleEdit.setText(book.title)
        binding.editTextBooksAuthorEdit.setText(book.author)
        binding.editTextBooksPublisherEdit.setText(book.publisher)
        binding.textViewDateOfReleaseEdit.text = book.dateOfRelease
        binding.editTextAmountOfPagesEdit.setText(book.amountOfPages.toString())
    }

    private fun pickDate(){
        DatePickerDialog(
            this, {
                    _,
                    mYear,
                    mMonth,
                    mDay ->
                text_view_date_of_release_edit.text = "$mYear-$mMonth-$mDay"
            }, year, month, day)
            .show()
    }

    private fun getCurrentDate(): String{
        return "$year-${month+1}-$day"
    }

    private fun postValidation(): Boolean{
        if(binding.editTextBooksTitleEdit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_title, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextBooksAuthorEdit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_author, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextBooksPublisherEdit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_publisher, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.textViewDateOfReleaseEdit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_date, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextAmountOfPagesEdit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_amount_of_pages, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun uploadBook(){

        if(!postValidation()) return

        val updatedBook = UpdateBook(
            binding.editTextBooksTitleEdit.text.toString(),
            binding.editTextBooksAuthorEdit.text.toString(),
            binding.spinnerCoverTypeEdit.selectedItem.toString(),
            binding.editTextBooksPublisherEdit.text.toString(),
            getCurrentDate(),
            binding.textViewDateOfReleaseEdit.text.toString(),
            binding.editTextAmountOfPagesEdit.text.toString(),
            userId
        )

        viewModel.update(book.id, updatedBook)

        Intent(this, MenuActivity::class.java).also {
            startActivity(it)
        }
    }

}