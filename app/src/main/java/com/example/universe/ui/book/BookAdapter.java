package com.example.universe.ui.book;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.List;

//The BookAdapter class is designed to connect data to a RecyclerView, allowing it to display a list of book items efficiently.


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<Book_unit> books; // List of Book_unit items
    //private final OnItemClickListener listener; // Item click listener
    //private final OnBookmarkClickListener bookmarkClickListener;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db;

    private Context context;




    // Constructor to initialize the data and the click listener
    public BookAdapter(List<Book_unit> books, Context context) {
        this.books = books;
        this.context = context;
        //this.bookmarkClickListener = bookmarkClickListener;
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
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

        Log.d("BookAdapter", "onBindViewHolder book"+book);

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

        Log.d("BookAdapter", "onBindViewHolder book userId:  "+book.getUserId());
        String userId = book.getUserId();
        String postId = book.getPostId();
        Log.d("BookAdapter", "userId: " + userId + ", postId: " + postId);

        DocumentReference postRef = db.collection("users").document(userId)
                .collection("posts").document(postId);

        Log.d("BookAdapter", "onBindViewHolder postRef  :  "+postRef);


        postRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String imageUrl = documentSnapshot.getString("image_url");
                // Use Glide or similar library to load the cover image
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.coverImageView);
                Log.d(TAG, "cover_url: " + imageUrl);
            } else {
                Log.d("BookAdapter", "Document does not exist");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching image URL: " + e.getMessage());
        });








        holder.itemView.setOnClickListener(v -> {
            Log.e("BookAdapter", "Create intent to BookActivity");
            // Create an intent to open BookActivity
            Intent intent = new Intent(context, BookActivity.class);

            // Pass additional data
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("reviewer", book.getReviewer());
            intent.putExtra("cover", book.getCover());
            intent.putExtra("rating", book.getRating());
            intent.putExtra("postId", book.getPostId());
            intent.putExtra("userId", book.getUserId());

            context.startActivity(intent); // Start the new activity
        });












        holder.bookmarkButton.setOnClickListener(v -> {
            // Toggle bookmark state
            boolean newBookmarkedState = !book.isBookmarked();
            book.setBookmarked(newBookmarkedState); // Update local data model

            // Change the bookmark icon/image
            if (newBookmarkedState) {
                holder.bookmarkButton.setImageResource(R.drawable.guardar_instagram); // Example icon
            } else {
                holder.bookmarkButton.setImageResource(R.drawable.marcador_de_forma_negra); // Example icon
            }

            // Save the new state to Firestore
            saveBookmarkStatusToFirestore(book); // Save to Firebase
        });

        // Set the initial bookmark icon based on the current state
        if (book.isBookmarked()) {
            holder.bookmarkButton.setImageResource(R.drawable.guardar_instagram);
        } else {
            holder.bookmarkButton.setImageResource(R.drawable.marcador_de_forma_negra);
        }













    }





    private void saveBookmarkStatusToFirestore(Book_unit book) {
        // Get the current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("BookAdapter", "User not logged in");
            return; // Ensure user is logged in
        }

        String userId = currentUser.getUid(); // Get user ID


        if (book.isBookmarked()) {
            // Add to "bookmarks" collection
            db.collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .document(book.getPostId()) // Use the book's unique ID
                    .set(book) // Save the book information
                    .addOnSuccessListener(aVoid -> Log.d("BookAdapter", "Bookmark added" ))
                    .addOnFailureListener(e -> Log.e("BookAdapter", "Failed to add bookmark", e));
        } else {
            // Remove from "bookmarks" collection
            db.collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .document(book.getPostId()) // Remove the document
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("BookAdapter", "Bookmark removed"))
                    .addOnFailureListener(e -> Log.e("BookAdapter", "Failed to remove bookmark", e));
        }
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
            Log.d("BookAdapter", "BookViewHolder" );
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