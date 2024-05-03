package com.example.universe.ui.search;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universe.R;
import com.example.universe.ui.book.BookAdapter;
import com.example.universe.ui.book.Book_unit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FirebaseFirestore db;
    private BookAdapter bookAdapter;

    private List<Book_unit> books = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Find the Spinner in the inflated layout
        Spinner spinner = rootView.findViewById(R.id.categories_search);
        EditText searchEditText = rootView.findViewById(R.id.search);
        Button searchButton = rootView.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    // Perform search
                    searchBooks(searchText);
                } else {
                    // Show error message or handle empty search query
                    searchBooksByCategory(spinner.getSelectedItem().toString());
                }
            }
        });

        populateSpinner(spinner);

        // Define your list of categories
        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("All");
        categoriesList.add("History");
        categoriesList.add("Thriller");
        categoriesList.add("Sci-Fi");
        categoriesList.add("Horror");
        categoriesList.add("Fantasy");
        categoriesList.add("Romance");
        categoriesList.add("Adventure");
        categoriesList.add("Self-help");
        categoriesList.add("Essay");
        categoriesList.add("Comic/Graphic novel");
        categoriesList.add("Manga");
        categoriesList.add("Children");
        categoriesList.add("Poetry");
        categoriesList.add("Cook-book");
        categoriesList.add("Travel");
        categoriesList.add("Other");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set the prompt text programmatically
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setPrompt("Select a category");
            }
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookAdapter = new BookAdapter(books, getContext());
        recyclerView.setAdapter(bookAdapter);

        return rootView;
    }

    private void populateSpinner(Spinner spinner) {
        List<String> categoriesList = new ArrayList<>();
        // Add categories to the list
        // (You can use your existing code here)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void searchBooks(String query) {
        // Perform search query in Firestore
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    // Handle error if the task failed
                    Log.e(TAG, "Error fetching users", task.getException()); // Log errors
                    return;
                }
                // Clear the existing list of books
                books.clear();

                // Iterate through all user documents
                for (QueryDocumentSnapshot userDoc : task.getResult()) {
                    String userId = userDoc.getId();
                    // Query each user's "posts" subcollection
                    db.collection("users").document(userDoc.getId()).collection("posts")
                            .whereEqualTo("title", query)
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
                                        String reviewer = userDoc.getString("username");
                                        Double ratingValue = postDoc.getDouble("rating"); // Retrieve as Double
                                        float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f; // Convert to float with a default value if null
                                        Boolean isBookmarkedValue = postDoc.getBoolean("isBookmarked");
                                        boolean isBookmarked = (isBookmarkedValue != null) ? isBookmarkedValue.booleanValue() : false;
                                        String postId = postDoc.getId();

                                        books.add(new Book_unit(title, author, cover, reviewer, rating, isBookmarked, postId, userId));
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

    private void searchBooksByCategory(String category) {
        // Perform search query in Firestore
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    // Handle error if the task failed
                    Log.e(TAG, "Error fetching users", task.getException()); // Log errors
                    return;
                }
                // Clear the existing list of books
                books.clear();

                // Iterate through all user documents
                for (QueryDocumentSnapshot userDoc : task.getResult()) {
                    String userId = userDoc.getId();
                    // Query each user's "posts" subcollection
                    db.collection("users").document(userDoc.getId()).collection("posts")
                            .whereEqualTo("category", category.equals("All") ? "" : category)
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
                                        String reviewer = userDoc.getString("username");
                                        Double ratingValue = postDoc.getDouble("rating"); // Retrieve as Double
                                        float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f; // Convert to float with a default value if null
                                        Boolean isBookmarkedValue = postDoc.getBoolean("isBookmarked");
                                        boolean isBookmarked = (isBookmarkedValue != null) ? isBookmarkedValue.booleanValue() : false;
                                        String postId = postDoc.getId();

                                        books.add(new Book_unit(title, author, cover, reviewer, rating, isBookmarked, postId, userId));
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
