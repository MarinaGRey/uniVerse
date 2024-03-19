package com.example.universedefinitivo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.universedefinitivo.ui.formulario.FormularioActivity;
import com.example.universedefinitivo.ui.login.LoginActivity;
import com.example.universedefinitivo.ui.profile.SavedActivity;
import com.example.universedefinitivo.ui.profile.YourPostsActivity;

public class ProfileFragment extends Fragment {
    private Button yourPostsButton;
    private Button savedButton;
    private Button addPostButton;
    private ImageButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find views in the fragment layout
        yourPostsButton = rootView.findViewById(R.id.your_posts_button);
        savedButton = rootView.findViewById(R.id.saved_button);
        addPostButton = rootView.findViewById(R.id.add_post_button);
        logoutButton = rootView.findViewById(R.id.logout_button);

        // Set OnClickListener for yourPostsButton
        yourPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the YourPostsActivity when yourPostsButton is clicked
                Intent intent = new Intent(getActivity(), YourPostsActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for savedButton
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SavedActivity when savedButton is clicked
                Intent intent = new Intent(getActivity(), SavedActivity.class);
                startActivity(intent);
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

        return rootView;
    }
}
