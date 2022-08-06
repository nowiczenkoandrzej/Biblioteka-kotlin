package com.nowiczenko.andrzej.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowiczenko.andrzej.biblioteka.BookDetailActivity
import com.nowiczenko.andrzej.api.MyApi
import com.nowiczenko.andrzej.biblioteka.*
import com.nowiczenko.andrzej.otherClasses.BookAdapter
import com.nowiczenko.andrzej.otherClasses.BookItem
import kotlinx.android.synthetic.main.fragment_books.*
import kotlinx.android.synthetic.main.fragment_books.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await


class BooksFragment : Fragment() {

    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_books, container, false)

        view.swipe_to_refresh.setOnRefreshListener {
            getBooksRequest()
            swipe_to_refresh.isRefreshing = false
        }

        recyclerView = view.findViewById(R.id.recycle_view_all_books)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBooksRequest()
    }

    private fun getBooksRequest(){
        CoroutineScope(Dispatchers.IO).launch {
            val books = MyApi().getBook().await()
            setRecyclerView(books)
        }
    }

    private suspend fun setRecyclerView(books: List<BookItem>){
        withContext(Dispatchers.Main){
            val layoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            val finalList = books.asReversed()
            bookAdapter = BookAdapter(finalList){ book ->
                val intent = Intent(activity, BookDetailActivity::class.java)
                intent.putExtra("book", book)
                activity?.startActivity(intent)
            }
            recyclerView.adapter = bookAdapter
        }
    }


}



//Stary sposób pobierania ksiazek

//private fun getBooks() {
//
//    val retrofitBooks = MyApi().getBook()
//
//    retrofitBooks.enqueue(object : Callback<List<BookItem>?> {
//        override fun onResponse(
//            call: Call<List<BookItem>?>,
//            response: Response<List<BookItem>?>
//        ) {
//            val responseBody = response.body()!!
//            val layoutManager = LinearLayoutManager(context)
//            recyclerView = view!!.findViewById(R.id.recycle_view_all_books)
//            recyclerView.layoutManager = layoutManager
//            recyclerView.setHasFixedSize(true)
//            bookAdapter = BookAdapter(responseBody)
//            recyclerView.adapter = bookAdapter
//        }
//
//        override fun onFailure(call: Call<List<BookItem>?>, t: Throwable) {
//            Log.d("BooksFragment", "onFailure: " + t.message)
//        }
//    })
//}


