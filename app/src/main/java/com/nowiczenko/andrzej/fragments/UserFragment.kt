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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await


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
        getBooksRequest()
        setMessage()
    }

    override fun onStart() {
        super.onStart()
        getBooksRequest()
        setMessage()
    }

    private fun setMessage(){
        text_view_user_panel.text = "Witaj ${getUsernameById(userId)}, oto Twoje książki:"
    }

    private fun getBooksRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            val books = MyApi().getBook().await()
            setRecycleView(books)
        }
    }

    private suspend fun setRecycleView(books: List<BookItem>){
        withContext(Dispatchers.Main){
            val layoutManager = LinearLayoutManager(context)
            recyclerView = requireView().findViewById(R.id.recycle_view_user_books)
            recyclerView.layoutManager = layoutManager
            val usersBooksList = getUsersBooks(books)
            bookAdapter = BookAdapter(usersBooksList)
            recyclerView.adapter = bookAdapter
        }
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
}
