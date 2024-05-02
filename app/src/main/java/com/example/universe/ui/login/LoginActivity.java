package com.example.universe.ui.login;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.universe.MainActivity;
import com.example.universe.R;
import com.example.universe.ui.formulario.FormularioActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        // PROVISIONAL ENTER
        View enter = findViewById(R.id.my_image_view);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askNotificationPermission();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish login activity
            }
        });

        View login_button = findViewById(R.id.login_button);
        View sign_up_button = findViewById(R.id.sign_up_button);
        EditText username_text = findViewById(R.id.username_text);
        username_text.setVisibility(View.GONE);
        View confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setVisibility(View.GONE);

        sign_up_button.setOnClickListener(v -> {
            // Toggle visibility of map modal layout
            if (username_text.getVisibility() == View.GONE) {
                username_text.setVisibility(View.VISIBLE);
                login_button.setVisibility(View.GONE);
                sign_up_button.setVisibility(View.GONE);
                confirm_button.setVisibility(View.VISIBLE);
            }
        });

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve email, password, and username from EditText fields
                EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword);
                EditText usernameEditText = findViewById(R.id.username_text);

                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                final String username = usernameEditText.getText().toString().trim();

                // Check if email or password is empty
                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Something is empty.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Perform user creation with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User creation success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid(); // Get Firebase-generated UID

                                        // Store additional user details (username) in Firestore
                                        Map<String, String> userData = new HashMap<>();
                                        userData.put("username", username);
                                        userData.put("email", email);

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("users")
                                                .document(userId)
                                                .set(userData);


                                        // Proceed to the main activity
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish(); // Finish login activity
                                    } else {
                                        // Handle null user
                                        Toast.makeText(LoginActivity.this, "User is null.", Toast.LENGTH_SHORT).show();
                                    }
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


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that your app will not show notifications.
                }
            });


    // Method to request notification permission
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the app already has the permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Request permission directly
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}