package com.example.universe.ui.login;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

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
        View login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(view -> {
            Intent intent= new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        });

        View sign_up_button = findViewById(R.id.sign_up_button);
        EditText username_text = findViewById(R.id.username_text);
        username_text.setVisibility(View.GONE);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal layout
                if (username_text.getVisibility() == View.GONE) {
                    username_text.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
