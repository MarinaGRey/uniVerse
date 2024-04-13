package com.example.universe.ui.login;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.universe.MainActivity;
import com.example.universe.R;
import com.example.universe.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        View login_button = findViewById(R.id.login_button);
        View sign_up_button = findViewById(R.id.sign_up_button);
        EditText username_text = findViewById(R.id.username_text);
        username_text.setVisibility(View.GONE);
        View confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setVisibility(View.GONE);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal layout
                if (username_text.getVisibility() == View.GONE) {
                    username_text.setVisibility(View.VISIBLE);
                    login_button.setVisibility(View.GONE);
                    sign_up_button.setVisibility(View.GONE);
                    confirm_button.setVisibility(View.VISIBLE);
                }
            }
        });

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword);

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty email.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty password.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Perform user creation
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User creation success
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Finish login activity so user can't go back to it using back button
                                } else {
                                    // User creation failed
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword);

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty email.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty password.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Perform login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User login success
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Finish login activity so user can't go back to it using back button
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });












    }
}