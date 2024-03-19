package com.example.universedefinitivo.ui.login;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import com.example.universedefinitivo.R;
import com.example.universedefinitivo.ui.home.HomeActivity;
import com.example.universedefinitivo.ui.profile.YourPostsActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        // Button Log-in -> Go to main activity
        View button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Intent intent= new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);

        });

    }


}
