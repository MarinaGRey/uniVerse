package com.example.universedefinitivo.ui.profile;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.universedefinitivo.R;
import com.example.universedefinitivo.ui.formulario.FormularioActivity;
import com.example.universedefinitivo.ui.login.LoginActivity;

public class ProfileFragment extends Fragment {

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

        return rootView;
    }
}
