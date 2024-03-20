package com.example.universe.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.universe.R;
import com.example.universe.ui.book.BookActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the ImageButtons in the fragment layout
        ImageButton bookButton = rootView.findViewById(R.id.cover_book_view);
        ImageButton bookmarkButton = rootView.findViewById(R.id.bookmark_button);
        final boolean[] isBookmarked = {false};

        // Set OnClickListener for bookmark button
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

        // Set OnClickListener for book button
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BookActivity
                startActivity(new Intent(getActivity(), BookActivity.class));
            }
        });

        // Return the inflated view
        return rootView;
    }
}
