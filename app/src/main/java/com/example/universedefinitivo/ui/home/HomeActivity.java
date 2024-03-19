package com.example.universedefinitivo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.universedefinitivo.R;
import com.example.universedefinitivo.ui.book.BookActivity;
import com.example.universedefinitivo.ui.map.MapActivity;
import com.example.universedefinitivo.ui.profile.YourPostsActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // ImageButton searchButton = findViewById(R.id.imageButton_search);
        ImageButton bookButton = findViewById(R.id.cover_book_view);
        ImageButton bookmarkButton = findViewById(R.id.bookmark_button);
        final boolean[] isBookmarked = {false};

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle bookmark state
                isBookmarked[0] = !isBookmarked[0];

                // Change image resource based on bookmark state
                int drawableId = isBookmarked[0] ? R.drawable.guardar_instagram : R.drawable.marcador_de_forma_negra;
                bookmarkButton.setImageResource(drawableId);
            }
        });


        /*
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start HomeActivity again or do any other action
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });
        */
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start HomeActivity again or do any other action
                startActivity(new Intent(HomeActivity.this, BookActivity.class));
            }
        });

    }
}
