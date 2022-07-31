package com.nowiczenko.andrzej.biblioteka


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_items.view.*




class BookAdapter(
    private val bookList: List<BookItem>,
    private val listener: (BookItem) -> Unit
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val picasso = Picasso.get()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        picasso.load(bookList[position].image)
            .into(holder.bookImage)

        holder.bookTitle.text = bookList[position].title
        holder.bookAuthor.text = bookList[position].author
        holder.bookPublisher.text = "Wydawca: " + bookList[position].publisher
        holder.dateOfRelease.text = "Data wydania: " + bookList[position].dateOfRelease
        holder.amountOfPages.text = "Ilość stron: " + bookList[position].amountOfPages.toString()
        holder.user.text = "Dodano przez: " + getUsernameById(bookList[position].user.toString())


        holder.itemView.setOnClickListener { listener(bookList[position]) }
    }


    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var bookTitle: TextView
        var bookAuthor: TextView
        var bookPublisher: TextView
        var dateOfRelease: TextView
        var amountOfPages: TextView
        var user: TextView
        var bookImage: ImageView

        init {
            bookTitle = itemView.text_view_book_title
            bookAuthor = itemView.text_view_book_author
            bookPublisher = itemView.text_view_book_publisher
            dateOfRelease = itemView.text_view_release_date
            amountOfPages = itemView.text_view_amount_of_pages
            user = itemView.text_view_added_by
            bookImage = itemView.image_view_cover
        }

    }
}
