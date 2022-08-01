package com.nowiczenko.andrzej.biblioteka

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.api.UpdateBook
import com.nowiczenko.andrzej.otherClasses.BookItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await
import java.util.*

class EditActivity : AppCompatActivity() {

    private lateinit var book: BookItem

    //Calendar
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        book = intent.getSerializableExtra("book") as BookItem

        setListeners()
        setContent(book)
        setSpinnerAdapter()
    }

    private fun setListeners(){

        button_publish_edit.setOnClickListener {
            uploadBook()
        }

        text_view_date_of_release_edit.setOnClickListener {
            pickDate()
        }
    }

    private fun setSpinnerAdapter(){
        val spinnerValues: Array<String> = arrayOf("Miękka", "Twarda")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues)
        spinner_cover_type_edit.adapter = spinnerAdapter
    }

    private fun setContent(book: BookItem){
        Picasso.get().load(book.image).into(image_view_edit_book)
        edit_text_books_title_edit.setText(book.title)
        edit_text_books_author_edit.setText(book.author)
        edit_text_books_publisher_edit.setText(book.publisher)
        text_view_date_of_release_edit.text = book.dateOfRelease
        edit_text_amount_of_pages_edit.setText(book.amountOfPages.toString())
    }

    private fun pickDate(){
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener {
                    view,
                    mYear,
                    mMonth,
                    mDay ->
                text_view_date_of_release_edit.setText("$mYear-$mMonth-$mDay")
            }, year, month, day)
            .show()
    }

    private fun getCurrentDate(): String{
        return "$year-${month+1}-$day"
    }

    private fun postValidation(): Boolean{
        if(edit_text_books_title_edit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_title, Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_books_author_edit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_author, Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_books_publisher_edit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_publisher, Toast.LENGTH_SHORT).show()
            return false
        }
        if(text_view_date_of_release_edit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_date, Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_amount_of_pages_edit.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_amount_of_pages, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun uploadBook(){

        if(!postValidation()) return

        val updatedBook = UpdateBook(
            edit_text_books_title_edit.text.toString(),
            edit_text_books_author_edit.text.toString(),
            spinner_cover_type_edit.selectedItem.toString(),
            edit_text_books_publisher_edit.text.toString(),
            getCurrentDate(),
            text_view_date_of_release_edit.text.toString(),
            edit_text_amount_of_pages_edit.text.toString(),
            userId
        )

        CoroutineScope(Dispatchers.IO).launch {
            MyApi().updateBook(book.id, updatedBook).await()
        }

        Intent(this, MenuActivity::class.java).also {
            startActivity(it)
        }
    }

}