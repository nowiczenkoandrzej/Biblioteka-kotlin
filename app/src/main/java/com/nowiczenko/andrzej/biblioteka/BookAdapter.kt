package com.nowiczenko.andrzej.biblioteka


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_items.view.*


class BookAdapter(val bookList: List<BookItem>) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val picasso = Picasso.get()

    private fun getPublishingDate(position: Int): String {
        if (bookList[position].dateOfPublishing == null)
            return ""
        else
            return bookList[position].dateOfPublishing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        picasso.load(bookList[position].image)
            .into(holder.bookImage)

        holder.bookTitle.text = bookList[position].title
        holder.bookAuthor.text = bookList[position].author
        holder.bookCover.text = "Rodzaj okładki: " + bookList[position].cover
        holder.bookPublisher.text = "Wydawca: " + bookList[position].publisher
        holder.dateOfRelease.text = "Data wydania: " + bookList[position].dateOfRelease
        holder.dateOfPublishing.text = "Data publikacji: " + getPublishingDate(position)
        holder.amountOfPages.text = "Ilość stron: " + bookList[position].amountOfPages.toString()
        holder.user.text = "Dodano przez: " + getUsernameById(bookList[position].user.toString())

    }


    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var bookTitle: TextView
        var bookAuthor: TextView
        var bookCover: TextView
        var bookPublisher: TextView
        var dateOfRelease: TextView
        var dateOfPublishing: TextView
        var amountOfPages: TextView
        var user: TextView
        var bookImage: ImageView

        init {
            bookTitle = itemView.text_view_book_title
            bookAuthor = itemView.text_view_book_author
            bookCover = itemView.text_view_book_cover
            bookPublisher = itemView.text_view_book_publisher
            dateOfRelease = itemView.text_view_release_date
            dateOfPublishing = itemView.text_view_publishing_date
            amountOfPages = itemView.text_view_amount_of_pages
            user = itemView.text_view_added_by
            bookImage = itemView.image_view_cover
        }

    }
}
