package com.example.universedefinitivo.ui.formulario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.universedefinitivo.R;
import com.example.universedefinitivo.ui.book.BookActivity;

import java.util.ArrayList;
import java.util.List;

public class FormularioActivity extends Activity {

    private static final int PICK_FILE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario); // Change to your layout file name

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
                Intent intent = new Intent(FormularioActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });

        Button insertImageButton = findViewById(R.id.insert_image_button); // Change to your button ID
        insertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pick a file from the device
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Set the MIME type to allow any type of file

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
                // Get the URI of the selected file
                // You can do whatever you want with this URI, such as saving it or processing it further
                // Example:
                // Uri selectedFileUri = data.getData();
            }
        }
    }
}
