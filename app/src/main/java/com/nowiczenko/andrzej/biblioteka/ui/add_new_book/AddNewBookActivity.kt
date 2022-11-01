package com.nowiczenko.andrzej.biblioteka.ui.add_new_book


import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.retrofit.UploadRequestBody
import com.nowiczenko.andrzej.biblioteka.databinding.ActivityAddNewBookBinding
import com.nowiczenko.andrzej.biblioteka.utils.getFileName
import com.nowiczenko.andrzej.biblioteka.retrofit.PostBookItem
import com.nowiczenko.andrzej.biblioteka.ui.login.userId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_new_book.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

@AndroidEntryPoint
class AddNewBookActivity : AppCompatActivity() {


    private val viewModel: AddNewBookViewModel by viewModels()

    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var imageUri: Uri? = null
    private val IMAGE_REQUEST_CODE = 100
    private lateinit var binding: ActivityAddNewBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBookBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSpinnerAdapter()
        setListeners()
    }

    private fun setSpinnerAdapter(){
        val values = arrayOf("Miękka", "Twarda")
        binding.spinnerCoverType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, values)
    }

    private fun setListeners(){

        binding.imageViewAddNewBook.setOnClickListener {
            pickImageFromGallery()
        }

        binding.buttonPublish.setOnClickListener {
            uploadBook()
        }

        binding.textViewDateOfRelease.setOnClickListener {
            pickDate()
        }
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageUri = data?.data
            binding.imageViewAddNewBook.setImageURI(imageUri)
        }
    }

    private fun uploadBook(){

        if(!postValidation()) return
        binding.progressBar.isVisible = true
        val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return
        val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file,"image")

        val book = PostBookItem(
            title = binding.editTextBooksTitle.text.toString(),
            author = binding.editTextBooksAuthor.text.toString(),
            cover = binding.spinnerCoverType.selectedItem.toString(),
            publisher = binding.editTextBooksPublisher.text.toString(),
            dateOfRelease = binding.textViewDateOfRelease.text.toString(),
            dateOfPublishing = getCurrentDate(),
            amountOfPages = binding.editTextAmountOfPages.text.toString(),
            user = userId
        )


        viewModel.upload(body, file.name, book)
        binding.progressBar.isVisible = false
        finish()
    }

    private fun postValidation(): Boolean{

        if(imageUri == null){
            Toast.makeText(this, R.string.toast_pick_picture, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextBooksTitle.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_title, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextBooksAuthor.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_author, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextBooksPublisher.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_publisher, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.textViewDateOfRelease.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_date, Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.editTextAmountOfPages.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_amount_of_pages, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun pickDate(){
        DatePickerDialog(
            this, {
                    _,
                    mYear,
                    mMonth,
                    mDay ->
                text_view_date_of_release.setText("$mYear-$mMonth-$mDay")
            }, year, month, day)
            .show()
    }

    private fun getCurrentDate(): String{
        return "$year-${month+1}-$day"
    }
}
