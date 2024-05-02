package com.example.universe.ui.book;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.universe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class BookActivity extends AppCompatActivity {

    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Views
    private EditText title_write;
    private TextView user_name_book_view;


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
        ImageView cover = findViewById(R.id.cover_book);


        // Retrieve intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String title_text = intent.getStringExtra("title");
            String author_text = intent.getStringExtra("author");
            String reviewer_name = intent.getStringExtra("reviewer");
            String postId = intent.getStringExtra("postId");
            String userId = intent.getStringExtra("userId");

            DocumentReference postRef = db.collection("users").document(userId)
                    .collection("posts").document(postId);

            // Retrieve data from Firestore document
            postRef.get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        // Log.d(TAG, "HFGBETGBETHNGH: ");
                        // Extract data from the document
                        String review_text = document.getString("review");
                        String category_text = document.getString("category");
                        String coverUrl = document.getString("cover");

                        // Populate views with book details
                        title.setText(title_text);
                        author.setText(author_text);
                        reviewer.setText(reviewer_name);
                        review.setText(review_text);
                        category.setText(category_text);

                        // Load cover image using Glide library
                        Glide.with(this)
                                .load(coverUrl)
                                .into(cover);
                    } else {
                        // Document does not exist
                    }
                } else {
                    // Task failed with an exception
                }
            });
        }
    }
}