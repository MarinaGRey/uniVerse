package com.example.universe.ui.book;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universe.R;
import com.bumptech.glide.Glide;

import java.time.Instant;
import java.util.List;

//The BookAdapter class is designed to connect data to a RecyclerView, allowing it to display a list of book items efficiently.


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<Book_unit> books; // List of Book_unit items
    private final OnItemClickListener listener; // Item click listener
    private final OnBookmarkClickListener bookmarkClickListener;

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(Book_unit book); // Called when an item is clicked
    }


    // Interface for bookmark clicks
    public interface OnBookmarkClickListener {
        void onBookmarkClick(Book_unit book); // Method to handle bookmark clicks
    }


    // Constructor to initialize the data and the click listener
    public BookAdapter(List<Book_unit> books, OnItemClickListener listener, OnBookmarkClickListener bookmarkClickListener) {
        this.books = books;
        this.listener = listener;
        this.bookmarkClickListener = bookmarkClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_unit, parent, false);
        return new BookViewHolder(view); // Return a new ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the book at the specified position
        Book_unit book = books.get(position);

        // Set the text fields
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.reviewerTextView.setText("@" + book.getReviewer());
        // Set the rating value for the RatingBar
        holder.ratingBar.setRating(book.getRating()); // Use setRating for RatingBar


        // Set the rating if a rating bar exists in the layout
        //if (holder.ratingTextView != null) {
        //    holder.ratingTextView.setText(String.format("Rating: %.1f", book.getRating()));
        //}

        // Use Glide or similar library to load the cover image
        Glide.with(holder.itemView.getContext())
                .load(book.getCover())
                .into(holder.coverImageView);


        // Set the bookmark state based on the data model
        int bookmarkIcon = book.isBookmarked() ? R.drawable.guardar_instagram : R.drawable.marcador_de_forma_negra;
        holder.bookmarkButton.setImageResource(bookmarkIcon);
        Log.d(TAG, "bookmarkIcon: " + bookmarkIcon); // Log post ID


        // Handle bookmark button clicks
        holder.bookmarkButton.setOnClickListener(v -> {
            if (bookmarkClickListener != null) {
                bookmarkClickListener.onBookmarkClick(book);
            }

        });




        // Handle item click event
        holder.itemView.setOnClickListener(v -> listener.onItemClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size(); // Return the total number of books
    }

    // ViewHolder class for the RecyclerView
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView reviewerTextView;
        RatingBar ratingBar; //  to display the rating

        ImageView coverImageView;
        ImageButton bookmarkButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            // Bind views from the item layout
            titleTextView = itemView.findViewById(R.id.book_title);
            authorTextView = itemView.findViewById(R.id.book_author);
            reviewerTextView = itemView.findViewById(R.id.book_reviewer);
            ratingBar = itemView.findViewById(R.id.book_rating);
            coverImageView = itemView.findViewById(R.id.book_cover);
            bookmarkButton = itemView.findViewById(R.id.bookmark_button);
        }
    }
}