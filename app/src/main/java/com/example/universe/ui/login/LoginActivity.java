package com.example.universe.ui.login;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import com.example.universe.MainActivity;
import com.example.universe.R;


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
            Intent intent= new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);

        });

    }


}
