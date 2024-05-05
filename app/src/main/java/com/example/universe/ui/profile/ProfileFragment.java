

package com.example.universe.ui.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.universe.R;
import com.example.universe.ui.book.BookActivity;
import com.example.universe.ui.book.BookAdapter;
import com.example.universe.ui.book.BookCustomItemDecoration;
import com.example.universe.ui.book.Book_unit;
import com.example.universe.ui.formulario.FormularioActivity;
import com.example.universe.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;




public class ProfileFragment extends Fragment {
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

    Button addPostButton;
    ImageButton logoutButton;
    TextView usernameText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "userId in profile: "+ userId);




        // Find views in the fragment layout
        yourPostsButton = rootView.findViewById(R.id.your_posts_button);
        savedButton = rootView.findViewById(R.id.saved_button);
        addPostButton = rootView.findViewById(R.id.add_post_button);
        logoutButton = rootView.findViewById(R.id.logout_button);
        usernameText = rootView.findViewById(R.id.username);



        recyclerView = rootView.findViewById(R.id.recycler_view_profile);

        // Set the margins for the first item and other items
        int firstItemMarginTop = 0; // Margin for the first item (adjust as needed)
        int otherItemsMarginTop = 0; // Margin for other items (typically zero or minimal)

        // Add the custom ItemDecoration to the RecyclerView
        recyclerView.addItemDecoration(new BookCustomItemDecoration(firstItemMarginTop, otherItemsMarginTop));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setVisibility(View.VISIBLE);


        Log.d(TAG, "Start bookAdapter");
        // Define the adapter with a click listener that starts BookActivity
        bookAdapter = new BookAdapter(currentBookList,getContext());


        recyclerView.setAdapter(bookAdapter);








        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Get the user document reference
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Retrieve the username from the document
                                    String username = document.getString("username");
                                    // Set the username to the TextView
                                    usernameText.setText(username);
                                }
                            } else {
                                // Handle failures
                            }
                        }
                    });
        }









        // Set OnClickListener for addPostButton
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the FormularioActivity when addPostButton is clicked
                Intent intent = new Intent(getActivity(), FormularioActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity when logoutButton is clicked
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });




        Log.d(TAG, "Start clicks");

        yourPostsButton.setOnClickListener(v -> {
            int pressedColor = ContextCompat.getColor(v.getContext(), R.color.profile_seleccionado);
            int notpressedColor = ContextCompat.getColor(v.getContext(), R.color.login_fondo_profile_sin_select);
            yourPostsButton.setBackgroundColor(pressedColor);
            savedButton.setBackgroundColor(notpressedColor);
            Log.d("Click", "Start clicks yourPostsButton");
            loadUserPosts();
        });



        savedButton.setOnClickListener(v -> {
            // Start the SavedActivity when savedButton is clicked
            int pressedColor = ContextCompat.getColor(v.getContext(), R.color.profile_seleccionado);
            int notpressedColor = ContextCompat.getColor(v.getContext(), R.color.login_fondo_profile_sin_select);
            savedButton.setBackgroundColor(pressedColor);
            yourPostsButton.setBackgroundColor(notpressedColor);
            Log.d("Click", "Start clicks savedButton");
            loadSavedPosts();;
        });







        return rootView;


    }






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
        savedPosts.clear(); // Clear existing list to avoid duplicates
        Log.d("ProfileFragment", "loadSavedPosts for user: " + userId);

        db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> bookmarks = task.getResult().getDocuments();
                        int bookmarkCount = bookmarks.size(); // Get the total count of bookmarks
                        int[] completedCount = {0}; // A counter for completed operations

                        // For each bookmarked post, fetch the actual post data
                        for (DocumentSnapshot bookmark : bookmarks) {
                            String bookmarkedPostId = bookmark.getId(); // Get the bookmarked post ID
                            String authorUserId = bookmark.getString("userId");

                            db.collection("users")
                                    .document(authorUserId)
                                    .collection("posts")
                                    .document(bookmarkedPostId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            // Retrieve data and add to savedPosts
                                            String title = documentSnapshot.getString("title");
                                            String author = documentSnapshot.getString("author");
                                            String cover = documentSnapshot.getString("cover");
                                            String reviewer = documentSnapshot.getString("reviewer");
                                            Double ratingValue = documentSnapshot.getDouble("rating");
                                            float rating = (ratingValue != null) ? ratingValue.floatValue() : 0.0f;
                                            Boolean isBookmarkedValue = documentSnapshot.getBoolean("isBookmarked");
                                            boolean isBookmarked = (isBookmarkedValue != null) ? isBookmarkedValue.booleanValue() : false;

                                            Book_unit book = new Book_unit(title, author, cover, reviewer, rating, isBookmarked, bookmarkedPostId, authorUserId);
                                            savedPosts.add(book); // Add to savedPosts

                                            Log.d("ProfileFragment", "Added saved post: " + book);

                                            // Increment the completed count and check if all are done
                                            completedCount[0]++;
                                            if (completedCount[0] == bookmarkCount) {
                                                // All operations are complete; update the adapter
                                                currentBookList.clear();
                                                currentBookList.addAll(savedPosts);
                                                bookAdapter.notifyDataSetChanged(); // Update the adapter
                                                Log.d("ProfileFragment", "Adapter updated with " + savedPosts.size() + " saved posts");
                                            }
                                        } else {
                                            Log.d("ProfileFragment", "Document does not exist for bookmarkedPostId: " + bookmarkedPostId);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ProfileFragment", "Failed to retrieve post for bookmarkedPostId: " + bookmarkedPostId, e);
                                        completedCount[0]++;
                                        if (completedCount[0] == bookmarkCount) {
                                            // Update the adapter even if there were errors
                                            currentBookList.clear();
                                            currentBookList.addAll(savedPosts);
                                            bookAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    } else {
                        Log.e("ProfileFragment", "Failed to retrieve bookmarks", task.getException());
                    }
                });
    }
















}
















