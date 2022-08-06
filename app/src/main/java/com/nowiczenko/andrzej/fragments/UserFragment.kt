package com.nowiczenko.andrzej.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowiczenko.andrzej.biblioteka.BookDetailActivity
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.biblioteka.*
import com.nowiczenko.andrzej.otherClasses.BookAdapter
import com.nowiczenko.andrzej.otherClasses.BookItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await


class UserFragment : Fragment() {

    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var message: TextView
    private var isListEmpty = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        recyclerView = view.findViewById(R.id.recycle_view_user_books)
        message = view.findViewById(R.id.text_view_user_panel)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBooksRequest()
    }

    override fun onStart() {
        super.onStart()
        getBooksRequest()
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
            recyclerView.layoutManager = layoutManager
            val usersBooksList = getUsersBooks(books)
            bookAdapter = BookAdapter(usersBooksList){ book ->

                val intent = Intent(activity, BookDetailActivity::class.java)
                intent.putExtra("book", book)
                activity?.startActivity(intent)

            }

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
        isListEmpty = resultList.isEmpty()
        setMessage()
        return resultList
    }

    private fun setMessage(){
        val textEnding =
            if(isListEmpty)
                activity?.getString(R.string.user_no_books)
            else
                activity?.getString(R.string.user_your_books)

        message.text = "Witaj ${getUsernameById(userId)}, ${textEnding}"
    }

}
