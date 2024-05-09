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

import java.util.List;


//The BookAdapter class is designed to connect data to a RecyclerView, allowing it to display a list of book items efficiently.
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<Book_unit> books; // List of Book_unit items
    private final FirebaseAuth firebaseAuth;

    private final FirebaseFirestore db;
    FirebaseUser currentUser;
    String CurrentuserId;

    private final Context context;




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
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        CurrentuserId = currentUser.getUid();

        Log.d("BookAdapter", "onBindViewHolder book"+book);

        // Set the text fields
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.reviewerTextView.setText("@" + book.getReviewer());
        // Set the rating value for the RatingBar
        holder.ratingBar.setRating(book.getRating()); // Use setRating for RatingBar

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
        }).addOnFailureListener(e -> Log.e(TAG, "Error fetching image URL: " + e.getMessage()));


        // Query to check if the book is bookmarked
        DocumentReference bookmarkRef = db.collection("users")
                .document(CurrentuserId)
                .collection("bookmarks")
                .document(book.getPostId());

        bookmarkRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // If the book is bookmarked, set the appropriate icon
                holder.bookmarkButton.setImageResource(R.drawable.marcador_de_forma_negra);
                book.setBookmarked(true); // Update local model
            } else {
                // If not bookmarked, set the other icon
                holder.bookmarkButton.setImageResource(R.drawable.guardar_instagram);
                book.setBookmarked(false); // Update local model
            }
        }).addOnFailureListener(e -> Log.e("BookAdapter", "Error checking bookmark status", e));







        // Toggle bookmark state on button click
        holder.bookmarkButton.setOnClickListener(v -> {
            boolean newBookmarkedState = !book.isBookmarked(); // Toggle the bookmark state
            book.setBookmarked(newBookmarkedState);

            if (newBookmarkedState) {
                holder.bookmarkButton.setImageResource(R.drawable.marcador_de_forma_negra); // Bookmarked
                saveBookmarkStatusToFirestore(book); // Add to Firestore bookmarks
            } else {
                holder.bookmarkButton.setImageResource(R.drawable.guardar_instagram); // Not bookmarked
                saveBookmarkStatusToFirestore(book); // Remove from Firestore bookmarks
            }
        });




        View.OnClickListener itemClickListener = v -> {
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
        };


        // Apply the click listener to the itemView
        holder.itemView.setOnClickListener(itemClickListener);

        // If you have an image view inside the item, apply the same click listener
        holder.coverImageView.setOnClickListener(itemClickListener);



    }





    private void saveBookmarkStatusToFirestore(Book_unit book) {

        if (currentUser == null) {
            Log.e("BookAdapter", "User not logged in");
            return;
        }

        String userId = currentUser.getUid(); // Get user ID

        if (book.isBookmarked()) {
            // Add to "bookmarks" collection
            db.collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .document(book.getPostId())
                    .set(book) // Save book info
                    .addOnSuccessListener(aVoid -> Log.d("BookAdapter", "Bookmark added"))
                    .addOnFailureListener(e -> Log.e("BookAdapter", "Failed to add bookmark", e));
        } else {
            // Remove from "bookmarks" collection
            db.collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .document(book.getPostId()) // Use the book's unique ID
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