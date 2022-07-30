package com.nowiczenko.andrzej.activities


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.api.PostBookItem
import com.nowiczenko.andrzej.api.UploadRequestBody
import com.nowiczenko.andrzej.biblioteka.R
import com.nowiczenko.andrzej.biblioteka.getFileName
import com.nowiczenko.andrzej.biblioteka.snackbar
import com.nowiczenko.andrzej.biblioteka.userId
import kotlinx.android.synthetic.main.activity_add_new_book.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AddNewBookActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    private var imageUri: Uri? = null
    val IMAGE_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_book)

        setListeners()
        setSpinnerAdapter()
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

    override fun onProgressUpdate(percentage: Int) {

    }

    private fun uploadBook(){
        if(imageUri == null){
            Toast.makeText(this, "Musisz wybrać obraz", Toast.LENGTH_SHORT).show()
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
                edit_text_date_of_release.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                edit_text_date_of_publishing.text.toString()
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
                finish()
            }

            override fun onFailure(call: Call<PostBookItem?>, t: Throwable) {
                finish()
            }
        })

    }

    private fun uploadBookRequest(){
        if(imageUri == null){
            Toast.makeText(this, "Musisz wybrać obraz", Toast.LENGTH_SHORT).show()
            return
        }

        if(!postValidation()) return

        val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return
        val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file,"image", this)

        CoroutineScope(Dispatchers.IO).launch {

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
                        edit_text_date_of_release.text.toString()
                    ),
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        edit_text_date_of_publishing.text.toString()
                    ),
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        edit_text_amount_of_pages.text.toString()
                    ),
                    MultipartBody.Part.createFormData("image", file.name, body),
                    RequestBody.create(MediaType.parse("multipart/form-data"), userId)
                ).await()
            finish()
        }

    }

    private fun postValidation(): Boolean{
        if(edit_text_books_title.text.toString() == ""){
            Toast.makeText(this, "Musisz podać tytuł książki", Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_books_author.text.toString() == ""){
            Toast.makeText(this, "Musisz podać autora książki", Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_books_publisher.text.toString() == ""){
            Toast.makeText(this, "Musisz podać wydawcę książki", Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_date_of_release.text.toString() == ""){
            Toast.makeText(this, "Musisz podać datę wydania książki", Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_date_of_publishing.text.toString() == ""){
            Toast.makeText(this, "Musisz podać datę publikacji książki", Toast.LENGTH_SHORT).show()
            return false
        }
        if(edit_text_amount_of_pages.text.toString() == ""){
            Toast.makeText(this, "Musisz podać ilość stron", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}