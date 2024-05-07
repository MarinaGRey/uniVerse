package com.example.universe.ui.book;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.universe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.example.universe.Notifications;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookActivity extends AppCompatActivity {

    // Firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the appropriate layout
        setContentView(R.layout.book);

        // Initialize Views
        TextView reviewer = findViewById(R.id.user_name_book_view);
        TextView title = findViewById(R.id.title_text);
        TextView author = findViewById(R.id.author_text);
        TextView review = findViewById(R.id.review);
        TextView category = findViewById(R.id.category_book_view);
        Button buy = findViewById(R.id.buyButton_book_view);
        ImageView cover_place = findViewById(R.id.cover_book);
        RatingBar rating_place = findViewById(R.id.book_review);
        EditText comment = findViewById(R.id.add_comment);
        Button add_comment = findViewById(R.id.add_comment_button);
        LinearLayout comments_layout = findViewById(R.id.comment_layout);

        Log.d("BookActivity", "Arrive to BookActivity!");

        // Retrieve intent extras
        Intent intent = getIntent();
        if (intent != null) {
            Log.d("BookActivity", "Intent not null");
            String title_text = intent.getStringExtra("title");
            String author_text = intent.getStringExtra("author");
            String reviewer_name = intent.getStringExtra("reviewer");
            String postId = intent.getStringExtra("postId");
            String userId = intent.getStringExtra("userId");

            assert userId != null;
            assert postId != null;
            DocumentReference postRef = db.collection("users").document(userId)
                    .collection("posts").document(postId);

            Log.d("BookActivity", "postRef"+postRef);

            // Retrieve data from Firestore document
            postRef.get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("BookActivity", "document exists");
                        // Extract data from the document
                        String review_text = document.getString("review");
                        String category_text = document.getString("category");
                        String coverUrl = document.getString("image_url");
                        String buyUrl = document.getString("link");
                        Double rating = document.getDouble("rating");

                        // Populate views with book details
                        title.setText(title_text);
                        author.setText(author_text);
                        reviewer.setText(reviewer_name);
                        review.setText(review_text);
                        category.setText(category_text);

                        float maxRating = 5.0f; // Maximum rating of the RatingBar
                        Log.d(TAG, "initial rate: " + rating);

                        float final_rating = (float) (rating / maxRating) * maxRating;
                        Log.d(TAG, "final rate: " + final_rating);

                        rating_place.setRating(final_rating);

                        // Load the image using Glide
                        Glide.with(this /* context */)
                                .load(coverUrl)
                                .into(cover_place);

                        // Set onClickListener for the buy button
                        buy.setOnClickListener(view -> {
                            // Open the buy URL in a web browser
                            openUrlInBrowser(buyUrl);
                        });

                        // Fetch existing comments from Firestore
                        postRef.collection("comments").get().addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot commentSnapshot : queryDocumentSnapshots) {
                                String username = commentSnapshot.getString("username");
                                String commentText = commentSnapshot.getString("text");

                                // Create a TextView for each existing comment and add it to the LinearLayout
                                TextView commentTextView = new TextView(BookActivity.this);
                                commentTextView.setText(username + ": " + commentText);
                                comments_layout.addView(commentTextView);
                            }
                        });

                        // Set onClickListener for the add comment button
                        // Set onClickListener for the add comment button
                        // Set onClickListener for the add comment button
                        add_comment.setOnClickListener(view -> {
                            // Retrieve the comment text from the EditText
                            String commentText = comment.getText().toString().trim();

                            // Check if the comment is not empty
                            if (!commentText.isEmpty()) {
                                // Get the current user ID from Firebase Authentication
                                String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                                // Create a new document reference for the comment
                                DocumentReference commentRef = postRef.collection("comments").document();

                                // Fetch the username associated with the current user ID
                                db.collection("users").document(currentUserId).get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                String currentUserName = documentSnapshot.getString("username");

                                                // Create a Map to hold the comment data
                                                Map<String, Object> commentData = new HashMap<>();
                                                commentData.put("text", commentText);
                                                commentData.put("username", currentUserName); // Add the username to the comment data

                                                // Add the comment data to the Firestore database
                                                commentRef.set(commentData)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Comment added successfully
                                                            Log.d(TAG, "Comment added to Firestore: " + commentText);

                                                            // After adding the comment, dynamically add it to the UI
                                                            TextView newCommentView = new TextView(BookActivity.this);
                                                            newCommentView.setText(currentUserName + ": " + commentText);
                                                            comments_layout.addView(newCommentView);

                                                            // Clear the EditText after adding the comment
                                                            comment.setText("");

                                                            // Trigger notification to post owner
                                                            Notifications.statusBar(view, userId);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Failed to add comment
                                                            Log.e(TAG, "Error adding comment to Firestore: " + e.getMessage());
                                                            // Handle the error
                                                        });
                                            } else {
                                                // Document does not exist
                                                Log.d(TAG, "User document does not exist for user ID: " + currentUserId);
                                                // Handle the case if the user document doesn't exist
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Failed to fetch username
                                            Log.e(TAG, "Error fetching username from Firestore: " + e.getMessage());
                                            // Handle the error
                                        });
                            } else {
                                Toast.makeText(BookActivity.this, "Comment is empty", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(BookActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Method to open a URL in a web browser
    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
