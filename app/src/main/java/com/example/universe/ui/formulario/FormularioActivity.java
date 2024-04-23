package com.example.universe.ui.formulario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.universe.R;
import com.example.universe.ui.book.BookActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioActivity extends Activity {

    private static final int PICK_FILE_REQUEST = 2;

    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId;

    // Views
    private EditText title_write;
    private EditText author_write;
    private EditText review_write;
    private EditText buy_link;
    private Spinner categories;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);

        // Get the current user's ID
        userId = auth.getCurrentUser().getUid();

        // Initialize Views
        title_write = findViewById(R.id.title_write);
        author_write = findViewById(R.id.author_write);
        review_write = findViewById(R.id.review_write);
        buy_link = findViewById(R.id.buy_link);
        categories = findViewById(R.id.categories);
        ratingBar = findViewById(R.id.ratingBar_formulario_view);

        // Initialize Spinner
        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("History");
        categoriesList.add("Thriller");
        categoriesList.add("Sci-Fi");
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        // Set Click Listeners
        Button postButton = findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to Firestore and BookActivity
                sendDataToFirestore();
            }
        });

        Button insertImageButton = findViewById(R.id.insert_image_button);
        insertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pick a file from the device
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");

                // Start the activity for result
                startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is the same as what is passed here
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                // Handle selected file here if needed
            }
        }
    }

    private void sendDataToFirestore() {
        // Get data from form fields
        String title = title_write.getText().toString().trim();
        String author = author_write.getText().toString().trim();
        String review = review_write.getText().toString().trim();
        String link = buy_link.getText().toString().trim();
        String category = categories.getSelectedItem().toString();
        float rating = ratingBar.getRating();

        // Add post data to Firestore
        Map<String, Object> post = new HashMap<>();
        post.put("title", title);
        post.put("author", author);
        post.put("review", review);
        post.put("link", link);
        post.put("category", category);
        post.put("rating", rating);

        // Add the post document to the current user's "posts" subcollection
        db.collection("users").document(userId).collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Post added successfully
                        Toast.makeText(FormularioActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                        // Proceed to BookActivity
                        sendDataToBookActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(FormularioActivity.this, "Error adding post", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendDataToBookActivity() {
        // Get data from form fields
        String title = title_write.getText().toString().trim();
        String author = author_write.getText().toString().trim();
        String review = review_write.getText().toString().trim();
        String link = buy_link.getText().toString().trim();
        String category = categories.getSelectedItem().toString();
        float rating = ratingBar.getRating();

        // Create intent to start BookActivity
        Intent intent = new Intent(FormularioActivity.this, BookActivity.class);
        // Pass data to BookActivity using extras or intent extras
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("review", review);
        intent.putExtra("link", link);
        intent.putExtra("category", category);
        intent.putExtra("rating", rating);

        // Start BookActivity
        startActivity(intent);
    }
}
