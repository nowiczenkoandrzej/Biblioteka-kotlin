package com.nowiczenko.andrzej.biblioteka.ui.books_screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowiczenko.andrzej.biblioteka.*
import com.nowiczenko.andrzej.biblioteka.databinding.FragmentBooksBinding
import com.nowiczenko.andrzej.biblioteka.retrofit.BookItem
import com.nowiczenko.andrzej.biblioteka.ui.book_detail.BookDetailActivity
import com.nowiczenko.andrzej.biblioteka.utils.StateEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {


    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentBooksBinding

    private val viewModel: BooksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBooksBinding.inflate(inflater, container,false)

        recyclerView = binding.root.findViewById(R.id.recycle_view_all_books)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.internetConnection(requireContext())

        subscribeObserver()
        setListeners()
    }

    private fun setListeners() {
        setRefreshButtonListener()
        setSwipeToRefreshListener()
    }

    private fun setSwipeToRefreshListener() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.get()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun setRefreshButtonListener() {
        binding.buttonRefresh.setOnClickListener {
            viewModel.internetConnection(requireContext())
        }
    }

    private fun subscribeObserver(){
        booksObserver()
        internetObserver()
    }

    private fun setRecyclerView(books: List<BookItem>){
            val layoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            val finalList = books.asReversed()
            bookAdapter = BookAdapter(finalList){ book ->
                Intent(activity, BookDetailActivity::class.java).also {
                    it.putExtra("book", book)
                    activity?.startActivity(it)
                }
            }
            recyclerView.adapter = bookAdapter
        }

    private fun booksObserver(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.books.collect{ event ->
                    when(event){
                        is StateEvent.Success<*> -> {
                            binding.progressBar.isVisible = false
                            binding.textViewErrors.isVisible = false
                            binding.buttonRefresh.isVisible = false
                            binding.recycleViewAllBooks.isVisible = true
                            setRecyclerView(event.result as List<BookItem>)
                        }
                        is StateEvent.Failure -> {
                            binding.recycleViewAllBooks.isVisible = false
                            binding.buttonRefresh.isVisible = true
                            binding.progressBar.isVisible = false
                            binding.textViewErrors.isVisible = true
                            binding.textViewErrors.text = event.error
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
                        setRecyclerView(emptyList())
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




