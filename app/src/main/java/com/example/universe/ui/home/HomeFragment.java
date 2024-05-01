package com.example.universe.ui.home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universe.R;
import com.example.universe.ui.book.BookActivity;
import com.example.universe.ui.book.BookAdapter;
import com.example.universe.ui.book.Book_unit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book_unit> books = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));



        // Define the adapter with a click listener that starts BookActivity
        bookAdapter = new BookAdapter(books,
                // Handle item click, like navigating to BookActivity
                book -> {
            Intent intent = new Intent(getContext(), BookActivity.class);

            // Pass additional data to the activity
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("reviewer", book.getReviewer());
            intent.putExtra("cover", book.getCover());
            intent.putExtra("rating", book.getRating());
            intent.putExtra("postId", book.getPostId());

            getContext().startActivity(intent); // Start the new activity
                    },

                book -> {
                    // Toggle bookmark state
                    book.setBookmarked(!book.isBookmarked()); // Toggle the state

                    // persist the bookmark state to Firestore or Shared Preferences
                    // update Firestore document to reflect the bookmark change
                    bookAdapter.notifyDataSetChanged(); // Refresh the RecyclerView to reflect changes
                });


        recyclerView.setAdapter(bookAdapter);

        // Fetch data from Firestore
        fetchAllUserPosts();


        return rootView;
    }

    private void fetchAllUserPosts() {
        // Step 1: Fetch all user documents from the "users" collection

        Log.e(TAG, "START fetchAllUserPosts "); // Log errors
        db.collection("users") // Reference to the "users" collection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            // Step 2: Handle error if the task failed
                            Log.e(TAG, "Error fetching users", task.getException()); // Log errors
                            return;
                        }
                        // Step 3: Clear the existing list of books
                        books.clear();
                        Log.d(TAG, "Successfully fetched users: " + task.getResult().size()); // Log number of users

                        // Step 4: Iterate through all user documents
                        for (DocumentSnapshot userDoc : task.getResult()) {
                            String userId = userDoc.getId();
                            Log.d(TAG, "Fetching posts for user: " + userDoc.getString("username")+ " id " + userId); // Log user ID

                            // Step 5: Query each user's "posts" subcollection
                            db.collection("users").document(userDoc.getId()).collection("posts")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> postTask) {
                                            if (!postTask.isSuccessful()) {
                                                // Handle error
                                                Log.e(TAG, "Error fetching posts", postTask.getException()); // Log errors
                                                return;
                                            }

                                            Log.d(TAG, "Successfully fetched posts: " + postTask.getResult().size()); // Log number of posts

                                            for (DocumentSnapshot postDoc : postTask.getResult()) {
                                                Log.d(TAG, "Processing post: " + postDoc.getId()); // Log post ID
                                                String title = postDoc.getString("title");
                                                String author = postDoc.getString("author");
                                                String cover = postDoc.getString("cover");
                                                String reviewer = postDoc.getString("reviewer");
                                                Double ratingValue = postDoc.getDouble("rating"); // Retrieve as Double
                                                float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f; // Convert to float with a default value if null
                                                Boolean isBookmarkedValue = postDoc.getBoolean("isBookmarked");
                                                boolean isBookmarked = (isBookmarkedValue != null) ? isBookmarkedValue.booleanValue() : false;
                                                String postId = postDoc.getString("postId");


                                                books.add(new Book_unit(title, author, cover, reviewer, rating,isBookmarked, postId));
                                                Log.d(TAG, "books: " + books); // Log post ID

                                            }

                                            if (bookAdapter != null) {
                                                bookAdapter.notifyDataSetChanged(); // Safely call notifyDataSetChanged()
                                            }

                                        }
                                    });
                        }
                    }
                });
    }








}
