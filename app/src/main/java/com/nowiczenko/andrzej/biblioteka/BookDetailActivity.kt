package com.nowiczenko.andrzej.biblioteka

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nowiczenko.andrzej.otherClasses.BookItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: BookItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        book = intent.getSerializableExtra("book") as BookItem
        setContent(book)

        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setContent(book: BookItem){
        text_view_book_detail_title.text = book.title
        text_view_book_detail_author.text = book.author
        text_view_book_detail_book_cover.text =  "${getString(R.string.detail_cover_type)}: ${book.cover}"
        text_view_book_detail_publisher.text = "${getString(R.string.detail_publisher)}: ${book.publisher}"
        text_view_book_detail_release_date.text = "${getString(R.string.detail_release_date)}: ${book.dateOfRelease}"
        text_view_book_detail_publishing_date.text = "${getString(R.string.detail_publishing_date)}: ${book.dateOfPublishing}"
        text_view_book_detail_amount_of_pages.text = "${getString(R.string.detail_amount_of_pages)}: ${book.amountOfPages}"
        text_view_book_detail_added_by.text = "${getString(R.string.detail_added_by)}: ${getUsernameById(book.user.toString())}"

        Picasso
            .get()
            .load(book.image)
            .into(image_view_book_detail)
    }

    private fun setListeners(){
        book_edit.setOnClickListener {
            Intent(this, EditActivity::class.java).also {
                it.putExtra("book", book)
                startActivity(it)
            }
        }
    }
}