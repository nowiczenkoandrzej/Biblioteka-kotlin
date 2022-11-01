package com.nowiczenko.andrzej.biblioteka.ui.book_detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityBookDetailBinding
import com.nowiczenko.andrzej.biblioteka.utils.getUsernameById
import com.nowiczenko.andrzej.biblioteka.retrofit.BookItem
import com.nowiczenko.andrzej.biblioteka.ui.edit_book.EditActivity
import com.nowiczenko.andrzej.biblioteka.ui.login.userId
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailActivity : AppCompatActivity() {


    private val picasso = Picasso.get()

    private val viewModel: BookDetailViewModel by viewModels()

    private lateinit var book: BookItem

    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getSerializableExtra("book") as BookItem
        setContent(book)

        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setContent(book: BookItem){
        binding.textViewBookDetailTitle.text = book.title
        binding.textViewBookDetailAuthor.text = book.author
        binding.textViewBookDetailBookCover.text =  "${getString(R.string.detail_cover_type)}: ${book.cover}"
        binding.textViewBookDetailPublisher.text = "${getString(R.string.detail_publisher)}: ${book.publisher}"
        binding.textViewBookDetailReleaseDate.text = "${getString(R.string.detail_release_date)}: ${book.dateOfRelease}"
        binding.textViewBookDetailPublishingDate.text = "${getString(R.string.detail_publishing_date)}: ${book.dateOfPublishing}"
        binding.textViewBookDetailAmountOfPages.text = "${getString(R.string.detail_amount_of_pages)}: ${book.amountOfPages}"
        binding.textViewBookDetailAddedBy.text = "${getString(R.string.detail_added_by)}: ${getUsernameById(book.user.toString())}"

        picasso
            .load(book.image)
            .into(binding.imageViewBookDetail)
    }

    private fun setListeners(){

        setEditListener()
        setDeleteListener()

    }

    private fun setEditListener(){

        binding.bookEdit.setOnClickListener {

            if(!userId.equals(book.user.toString())){
                Toast.makeText(this, R.string.toast_not_your_book, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Intent(this, EditActivity::class.java).also {
                it.putExtra("book", book)
                startActivity(it)
            }
        }
    }

    private fun setDeleteListener(){

        binding.bookDelete.setOnClickListener {

            if(!userId.equals(book.user.toString())){
                Toast.makeText(this, R.string.toast_not_your_book, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showDialog()
        }
    }

    private fun showDialog(){
        val dialog = AlertDialog.Builder(this)

        dialog.setMessage("Czy na pewno chcesz to zrobić?")

        dialog.setPositiveButton("Tak") { _, _ ->
            finish()
            viewModel.delete(book.id)
        }

        dialog.setNegativeButton("Nie") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }
}