package com.example.universe.ui.home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universe.R;
import com.example.universe.ui.book.BookAdapter;
import com.example.universe.ui.book.BookCustomItemDecoration;
import com.example.universe.ui.book.Book_unit;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class  HomeFragment extends Fragment {
    private final List<Book_unit> books = new ArrayList<>();
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

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_books);
        // Set the margins for the first item and other items
        int firstItemMarginTop = 0; // Margin for the first item (adjust as needed)
        int otherItemsMarginTop = 0; // Margin for other items (typically zero or minimal)

        // Add the custom ItemDecoration to the RecyclerView
        recyclerView.addItemDecoration(new BookCustomItemDecoration(firstItemMarginTop, otherItemsMarginTop));



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));



        // Define the adapter with a click listener that starts BookActivity
        BookAdapter bookAdapter = new BookAdapter(books, getContext());


        recyclerView.setAdapter(bookAdapter);

        // Fetch data from Firestore
        FirestoreUtils firestoreUtils = new FirestoreUtils(db);
        firestoreUtils.fetchAllUserPosts(books, bookAdapter);

        return rootView;
    }
    public static class FirestoreUtils {
        private final FirebaseFirestore db;

        public FirestoreUtils(FirebaseFirestore db) {
            this.db = db;
        }


        public void fetchAllUserPosts(List<Book_unit> books, BookAdapter bookAdapter) {
            // Step 1: Fetch all user documents from the "users" collection

            Log.e(TAG, "START fetchAllUserPosts "); // Log errors
            db.collection("users") // Reference to the "users" collection
                    .get()
                    .addOnCompleteListener(task -> {
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
                            Log.d(TAG, "Fetching posts for user: " + userDoc.getString("username") + " id " + userId); // Log user ID

                            // Step 5: Query each user's "posts" subcollection
                            db.collection("users").document(userDoc.getId()).collection("posts")
                                    .get()
                                    .addOnCompleteListener(postTask -> {
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
                                            String reviewer = userDoc.getString("username");
                                            Double ratingValue = postDoc.getDouble("rating"); // Retrieve as Double
                                            float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f; // Convert to float with a default value if null
                                            Boolean isBookmarkedValue = postDoc.getBoolean("isBookmarked");
                                            boolean isBookmarked = isBookmarkedValue != null && isBookmarkedValue;
                                            String postId = postDoc.getId();
                                            String userId1 = userDoc.getId();

                                            books.add(new Book_unit(title, author, cover, reviewer, rating, isBookmarked, postId, userId1));
                                            Log.d(TAG, "books: " + books); // Log post ID

                                        }

                                        if (bookAdapter != null) {
                                            bookAdapter.notifyDataSetChanged(); // Safely call notifyDataSetChanged()
                                        }

                                    });
                        }
                    });
        }
    }

}