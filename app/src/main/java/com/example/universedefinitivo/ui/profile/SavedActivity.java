package com.example.universedefinitivo.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.universedefinitivo.R; // Make sure to import R if it's in a different package
import com.example.universedefinitivo.ui.formulario.FormularioActivity;
import com.example.universedefinitivo.ui.home.HomeActivity;
import com.example.universedefinitivo.ui.login.LoginActivity;
import com.example.universedefinitivo.ui.map.MapActivity;
import com.example.universedefinitivo.ui.profile.YourPostsActivity;

public class SavedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_saved); // Assuming your layout file is named profile_saved.xml

        Button yourPostsButton = findViewById(R.id.your_posts_button);
        Button addPostButton = findViewById(R.id.add_post_button);
        ImageButton logoutButton = findViewById(R.id.logout_button);

        yourPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the YourPostsActivity when your_posts_button is clicked
                Intent intent = new Intent(SavedActivity.this, YourPostsActivity.class);
                startActivity(intent);
            }
        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the YourPostsActivity when your_posts_button is clicked
                Intent intent = new Intent(SavedActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the YourPostsActivity when your_posts_button is clicked
                Intent intent = new Intent(SavedActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}