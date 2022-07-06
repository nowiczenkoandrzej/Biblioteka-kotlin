package com.nowiczenko.andrzej.biblioteka


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.nowiczenko.andrzej.api.ApiInterfaceBooks
import kotlinx.android.synthetic.main.activity_book_list.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory




class BookListActivity : AppCompatActivity() {

    lateinit var bookAdapter: BookAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        setRecycleView()
        getBooks()

    }

    private fun setRecycleView(){

        recycle_view_books.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recycle_view_books.layoutManager = linearLayoutManager

    }


    private fun getBooks() {
        val api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterfaceBooks::class.java)

        val retrofitBooks = api.getBook()

        retrofitBooks.enqueue(object : Callback<List<BookItem>?> {
            override fun onResponse(
                call: Call<List<BookItem>?>,
                response: Response<List<BookItem>?>
            ) {
                val responseBody = response.body()!!

                bookAdapter = BookAdapter(baseContext, responseBody)
                bookAdapter.notifyDataSetChanged()
                recycle_view_books.adapter = bookAdapter
            }

            override fun onFailure(call: Call<List<BookItem>?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: " + t.message)
            }
        })
    }
}