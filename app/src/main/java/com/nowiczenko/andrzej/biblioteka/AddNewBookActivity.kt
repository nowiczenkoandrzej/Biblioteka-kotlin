package com.nowiczenko.andrzej.biblioteka


import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.api.PostBookItem
import com.nowiczenko.andrzej.api.UploadRequestBody
import kotlinx.android.synthetic.main.activity_add_new_book.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class AddNewBookActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    private var imageUri: Uri? = null
    private val IMAGE_REQUEST_CODE = 100

    //Calendar
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_book)

        setSpinnerAdapter()
        setListeners()
    }

    private fun setSpinnerAdapter(){
        val spinnerValues: Array<String> = arrayOf("Miękka", "Twarda")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues)
        spinner_cover_type.adapter = spinnerAdapter
    }

    private fun setListeners(){

        image_view_add_new_book.setOnClickListener {
            pickImageFromGallery()
        }

        button_publish.setOnClickListener {
            uploadBook()
        }

        text_view_date_of_release.setOnClickListener {
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
            image_view_add_new_book.setImageURI(imageUri)
        }
    }

    private fun uploadBook(){
        if(imageUri == null){
            Toast.makeText(this, R.string.toast_pick_picture, Toast.LENGTH_SHORT).show()
            return
        }

        if(!postValidation()) return

        val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return
        val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file,"image", this)

        MyApi().postBook(
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                edit_text_books_title.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                edit_text_books_author.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                spinner_cover_type.selectedItem.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                edit_text_books_publisher.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                getCurrentDate()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                text_view_date_of_release.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                edit_text_amount_of_pages.text.toString()
            ),
            MultipartBody.Part.createFormData("image", file.name, body),
            RequestBody.create(MediaType.parse("multipart/form-data"), userId)
        ).enqueue(object : Callback<PostBookItem?> {
            override fun onResponse(
                call: Call<PostBookItem?>,
                response: Response<PostBookItem?>
            ) {
                Log.d("TAG", "response")
                finish()
            }

            override fun onFailure(call: Call<PostBookItem?>, t: Throwable) {
                Log.d("TAG", "failure")
                finish()
            }
        })

    }

    private fun postValidation(): Boolean{
        if(edit_text_books_title.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_title, Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_books_author.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_author, Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_books_publisher.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_publisher, Toast.LENGTH_SHORT).show()
            return false
        }
        if(text_view_date_of_release.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_date, Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_amount_of_pages.text.toString() == ""){
            Toast.makeText(this, R.string.toast_pick_amount_of_pages, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun pickDate(){
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener {
                    view,
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

    override fun onProgressUpdate(percentage: Int) {

    }
}


//private fun uploadBookRequest(){
//    if(imageUri == null){
//        Toast.makeText(this, "Musisz wybrać obraz", Toast.LENGTH_SHORT).show()
//        return
//    }
//
//    if(!postValidation()) return
//
//    val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return
//    val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
//    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//    val outputStream = FileOutputStream(file)
//
//    inputStream.copyTo(outputStream)
//
//    val body = UploadRequestBody(file,"image", this)
//
//    CoroutineScope(Dispatchers.IO).launch {
//
//        MyApi().postBook(
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                edit_text_books_title.text.toString()
//            ),
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                edit_text_books_author.text.toString()
//            ),
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                spinner_cover_type.selectedItem.toString()
//            ),
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                edit_text_books_publisher.text.toString()
//            ),
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                edit_text_date_of_release.text.toString()
//            ),
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                edit_text_date_of_publishing.text.toString()
//            ),
//            RequestBody.create(
//                MediaType.parse("multipart/form-data"),
//                edit_text_amount_of_pages.text.toString()
//            ),
//            MultipartBody.Part.createFormData("image", file.name, body),
//            RequestBody.create(MediaType.parse("multipart/form-data"), userId)
//        ).await()
//        finish()
//    }
//
//}