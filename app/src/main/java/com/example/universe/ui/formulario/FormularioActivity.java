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

import com.example.universe.R;
import com.example.universe.ui.book.BookActivity;

import java.util.ArrayList;
import java.util.List;

public class FormularioActivity extends Activity {

    private static final int PICK_FILE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);

        Button postButton = findViewById(R.id.post_button);

        // Initialize Spinner
        Spinner spinner = findViewById(R.id.categories);

        // Define your list of categories
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

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from views and start BookActivity
                sendDataToBookActivity();
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

    private void sendDataToBookActivity() {
        EditText title_write = findViewById(R.id.title_write);
        String title = title_write.getText().toString().trim();

        EditText author_write = findViewById(R.id.author_write);
        String author = author_write.getText().toString().trim();

        EditText review_write = findViewById(R.id.review_write);
        String review = review_write.getText().toString().trim();

        EditText buy_link = findViewById(R.id.buy_link);
        String link = buy_link.getText().toString().trim();

        Spinner categories = findViewById(R.id.categories);
        String category = categories.getSelectedItem().toString();

        // Assuming you have a RatingBar named ratingBar in your layout XML file
        RatingBar ratingBar = findViewById(R.id.ratingBar_formulario_view);

        // Get the rating (number of stars)
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
