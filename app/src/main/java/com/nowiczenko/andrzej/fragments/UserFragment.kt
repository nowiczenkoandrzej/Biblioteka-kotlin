package com.nowiczenko.andrzej.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.biblioteka.*
import kotlinx.android.synthetic.main.fragment_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFragment : Fragment() {

    lateinit var bookAdapter: BookAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBooks()
        setMessage()
    }

    override fun onStart() {
        super.onStart()
        getBooks()
        setMessage()
    }

    private fun getBooks() {

        val retrofitBooks = MyApi().getBook()

        retrofitBooks.enqueue(object : Callback<List<BookItem>?> {
            override fun onResponse(
                call: Call<List<BookItem>?>,
                response: Response<List<BookItem>?>
            ) {
                val responseBody = response.body()!!
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view!!.findViewById(R.id.recycle_view_user_books)
                recyclerView.layoutManager = layoutManager
                val usersBooksList = getUsersBooks(responseBody)
                bookAdapter = BookAdapter(usersBooksList)
                recyclerView.adapter = bookAdapter
            }

            override fun onFailure(call: Call<List<BookItem>?>, t: Throwable) {
                Log.d("UserFragment", "onFailure: " + t.message)
            }
        })
    }

    private fun getUsersBooks(bookList: List<BookItem>): List<BookItem>{
        val resultList = ArrayList<BookItem>()
        for(book in bookList){
            if(book.user.toString().equals(userId)) {
                resultList.add(book)
            }
        }
        return resultList
    }

    private fun setMessage(){
        text_view_user_panel.text = "Witaj ${getUsernameById(userId)}, oto Twoje książki:"
    }

}