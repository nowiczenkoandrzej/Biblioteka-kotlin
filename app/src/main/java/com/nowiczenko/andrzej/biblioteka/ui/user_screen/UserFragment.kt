package com.nowiczenko.andrzej.biblioteka.ui.user_screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowiczenko.andrzej.biblioteka.*
import com.nowiczenko.andrzej.biblioteka.databinding.FragmentUserBinding
import com.nowiczenko.andrzej.biblioteka.retrofit.BookItem
import com.nowiczenko.andrzej.biblioteka.ui.book_detail.BookDetailActivity
import com.nowiczenko.andrzej.biblioteka.ui.books_screen.BookAdapter
import com.nowiczenko.andrzej.biblioteka.ui.books_screen.BooksViewModel
import com.nowiczenko.andrzej.biblioteka.ui.login.userId
import com.nowiczenko.andrzej.biblioteka.ui.login.userName
import com.nowiczenko.andrzej.biblioteka.utils.StateEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserFragment : Fragment() {



    private lateinit var binding: FragmentUserBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var message: TextView
    private var isListEmpty = true

    private val viewModel: BooksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        recyclerView = binding.root.findViewById(R.id.recycle_view_user_books)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.internetConnection(requireContext())


        setListeners()
        subscribeObserver()

    }

    private fun setListeners() {
        setRefreshButtonListener()
        setLogoutButtonListener()
    }


    private fun setRefreshButtonListener() {
        binding.buttonRefresh.setOnClickListener {
            viewModel.internetConnection(requireContext())
        }
    }

    private fun setLogoutButtonListener(){
        binding.imageLogout.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun setRecyclerView(books: List<BookItem>) {

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val usersBooksList = getUsersBooks(books)
        bookAdapter = BookAdapter(usersBooksList) { book ->
            val intent = Intent(activity, BookDetailActivity::class.java)
            intent.putExtra("book", book)
            activity?.startActivity(intent)
        }

        recyclerView.adapter = bookAdapter
    }


    private fun getUsersBooks(bookList: List<BookItem>): List<BookItem> {
        var counter = 0
        val resultList = ArrayList<BookItem>()
        for (book in bookList) {
            if (book.user.toString() == userId) {
                resultList.add(book)
                counter++
            }
        }
        isListEmpty = resultList.isEmpty()
        setMessage(counter)
        return resultList
    }

    private fun setMessage(booksAmount: Int) {
        binding.textViewUsername.text = "Witaj $userName"
        binding.textViewMessage.text = "Ilość dodanych książek: $booksAmount"
    }
    private fun subscribeObserver(){
        booksObserver()
        internetObserver()
    }

    private fun booksObserver(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.books.collect{ event ->
                    when(event){
                        is StateEvent.Success<*> -> {
                            binding.progressBar.isVisible = false
                            binding.recycleViewUserBooks.isVisible = true
                            setRecyclerView(event.result as List<BookItem>)
                        }
                        is StateEvent.Failure -> {
                            binding.progressBar.isVisible = false
                            binding.textViewErrors.isVisible = true
                            binding.textViewErrors.text = event.error
                            binding.recycleViewUserBooks.isVisible = false
                        }
                        is StateEvent.Loading -> {
                            binding.progressBar.isVisible = true
                            binding.textViewErrors.isVisible = false
                            binding.buttonRefresh.isVisible = false
                        }
                        is StateEvent.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun internetObserver(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.internetConnection.collect { connection ->
                    if(connection){
                        viewModel.get()
                    } else {
                        binding.recycleViewUserBooks.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.buttonRefresh.isVisible = true
                        binding.textViewErrors.isVisible = true
                        binding.textViewErrors.text = "Brak połączenia z internetem"
                    }
                }
            }
        }
    }

}
