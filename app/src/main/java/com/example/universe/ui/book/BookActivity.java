package com.example.universe.ui.book;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.universe.R;

public class BookActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the appropriate layout
        setContentView(R.layout.book);

        // Test if the layout displays properly
        TextView testTextView = findViewById(R.id.title_book);
        if (testTextView == null) {
            testTextView.setText("Book Activity Loaded Successfully");
        }

        /*
        // Retrieve the URL from the intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("webpageURL")) {
            String webpageURL = intent.getStringExtra("webpageURL");

            // Open the webpage
            openWebpage(webpageURL);
        } else {
            // Handle the case where the URL is not available
        }
    }

    private void openWebpage(String webpageURL) {
        // Create intent with ACTION_VIEW and the retrieved URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webpageURL));

        // Check if there's an app to handle this intent (a web browser)
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity to open the webpage
            startActivity(intent);
        } else {
            // Handle the case where there's no app to handle the intent (e.g., no web browser installed)
        }


         */
    }
}
