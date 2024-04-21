package com.example.universe.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.universe.R;
import com.example.universe.ui.formulario.FormularioActivity;
import com.example.universe.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;


public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find views in the fragment layout
        Button yourPostsButton = rootView.findViewById(R.id.your_posts_button);
        Button savedButton = rootView.findViewById(R.id.saved_button);
        Button addPostButton = rootView.findViewById(R.id.add_post_button);
        ImageButton logoutButton = rootView.findViewById(R.id.logout_button);
        TextView usernameText = rootView.findViewById(R.id.username);

        // Set OnClickListener for yourPostsButton
        yourPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pressedColor = ContextCompat.getColor(v.getContext(), R.color.profile_seleccionado);
                int notpressedColor = ContextCompat.getColor(v.getContext(), R.color.login_fondo_profile_sin_select);
                yourPostsButton.setBackgroundColor(pressedColor);
                savedButton.setBackgroundColor(notpressedColor);
            }
        });


        // Set OnClickListener for savedButton
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SavedActivity when savedButton is clicked
                int pressedColor = ContextCompat.getColor(v.getContext(), R.color.profile_seleccionado);
                int notpressedColor = ContextCompat.getColor(v.getContext(), R.color.login_fondo_profile_sin_select);
                savedButton.setBackgroundColor(pressedColor);
                yourPostsButton.setBackgroundColor(notpressedColor);
            }
        });

        // Set OnClickListener for addPostButton
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the FormularioActivity when addPostButton is clicked
                Intent intent = new Intent(getActivity(), FormularioActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity when logoutButton is clicked
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Retrieve and set the username
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Get the user document reference
            firestore.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Retrieve the username from the document
                                    String username = document.getString("username");
                                    // Set the username to the TextView
                                    usernameText.setText(username);
                                }
                            } else {
                                // Handle failures
                            }
                        }
                    });
        }

        return rootView;
    }
}
