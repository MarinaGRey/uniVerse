

package com.example.universe.ui.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universe.R;
import com.example.universe.ui.book.BookActivity;
import com.example.universe.ui.book.BookAdapter;
import com.example.universe.ui.book.BookCustomItemDecoration;
import com.example.universe.ui.book.Book_unit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment3 extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book_unit> currentBookList = new ArrayList<>();

    private List<Book_unit> userPosts = new ArrayList<>();
    private List<Book_unit> savedPosts = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String userId;
    Button yourPostsButton;
    Button savedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "userId in profile: "+ userId);

        yourPostsButton = rootView.findViewById(R.id.your_posts_button);
        savedButton = rootView.findViewById(R.id.saved_button);
        Log.d("Debug", "Button is enabled: " + yourPostsButton.isEnabled());
        Log.d("Debug", "Button is visible: " + (yourPostsButton.getVisibility() == View.VISIBLE));

        recyclerView = rootView.findViewById(R.id.recycler_view_profile);

        // Set the margins for the first item and other items
        int firstItemMarginTop = 800; // Margin for the first item (adjust as needed)
        int otherItemsMarginTop = 0; // Margin for other items (typically zero or minimal)

        // Add the custom ItemDecoration to the RecyclerView
        recyclerView.addItemDecoration(new BookCustomItemDecoration(firstItemMarginTop, otherItemsMarginTop));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));



        Log.d(TAG, "Start bookAdapter");
        // Define the adapter with a click listener that starts BookActivity
        bookAdapter = new BookAdapter(currentBookList,
                // Handle item click, like navigating to BookActivity
                book -> {
                    Intent intent = new Intent(getContext(), BookActivity.class);
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






        Log.d(TAG, "Start clicks");

        yourPostsButton.setOnClickListener(v -> {
            Log.d("Click", "Start clicks yourPostsButton");
            loadUserPosts();
        });



        savedButton.setOnClickListener(v -> {
            loadSavedPosts();;
        });



        return rootView;
    }




    /*
    yourPostsButton.setOnClickListener(v -> {
        Log.d("Click", "Start clicks yourPostsButton");
        updateAdapterWithUserPosts();
    });

        savedButton.setOnClickListener(v -> {
        updateAdapterWithSavedPosts();
    });

    // Load default data (user posts or saved posts)
    updateAdapterWithUserPosts();

        return rootView;
    private void updateAdapterWithUserPosts() {
        Log.w("Click", "Arrive updateAdapterWithUserPosts");
        loadUserPosts(); // Fetch data before updating the adapter
        currentBookList.clear();
        currentBookList.addAll(userPosts);
        bookAdapter.notifyDataSetChanged();
        Log.w("Click", "bookAdapter is: " + bookAdapter);
    }

    private void updateAdapterWithSavedPosts() {
        loadSavedPosts(); // Fetch data before updating the adapter
        currentBookList.clear();
        currentBookList.addAll(savedPosts);
        bookAdapter.notifyDataSetChanged();
    }


     */





    private void loadUserPosts() {
        userPosts.clear(); // Clear existing list to avoid duplicates
        Log.d("ProfileFragment", "Loading user posts for user: " + userId); // Check userId

        db.collection("users")
                .document(userId)
                .collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ProfileFragment", "Successfully loaded user posts");
                        if (task.getResult() != null) {


                            for (DocumentSnapshot postDoc : task.getResult()) {
                                // Create a Book_unit object and add it to savedPosts
                                String title = postDoc.getString("title");
                                String author = postDoc.getString("author");
                                String cover = postDoc.getString("cover");
                                String reviewer = postDoc.getString("reviewer");
                                Double ratingValue = postDoc.getDouble("rating"); // Retrieve as Double
                                float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f; // Convert to float with a default value if null
                                Boolean isBookmarkedValue = postDoc.getBoolean("isBookmarked");
                                boolean isBookmarked = (isBookmarkedValue != null) ? isBookmarkedValue.booleanValue() : false;
                                String postId = postDoc.getId();


                                userPosts.add(new Book_unit(title, author, cover, reviewer, rating, isBookmarked, postId,userId));
                                Log.d(TAG, "userPosts: " + userPosts);
                            }

                            currentBookList.clear();
                            currentBookList.addAll(userPosts);
                            bookAdapter.notifyDataSetChanged();


                        }
                        else {
                            Log.w("ProfileFragment", "No posts found");
                        }







                        //bookAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    }
                });
    }

    private void loadSavedPosts() {
        Log.d(TAG, "Start loadSavedPosts");
        savedPosts.clear(); // Clear existing list

        db.collection("users")
                .document(userId)
                .collection("bookmarks") // Assuming "bookmarks" is the collection for saved posts
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot postDoc : task.getResult()) {
                            // Create a Book_unit object and add it to savedPosts
                            String title = postDoc.getString("title");
                            String author = postDoc.getString("author");
                            String cover = postDoc.getString("cover");
                            String reviewer = postDoc.getString("reviewer");
                            Double ratingValue = postDoc.getDouble("rating"); // Retrieve as Double
                            float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f; // Convert to float with a default value if null
                            Boolean isBookmarkedValue = postDoc.getBoolean("isBookmarked");
                            boolean isBookmarked = (isBookmarkedValue != null) ? isBookmarkedValue.booleanValue() : false;
                            String postId = postDoc.getId();


                            savedPosts.add(new Book_unit(title, author, cover, reviewer, rating,isBookmarked, postId, userId));

                        }
                        bookAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    }
                });
    }













}
















