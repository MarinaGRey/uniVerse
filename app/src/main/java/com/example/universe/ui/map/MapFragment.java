package com.example.universe.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.universe.R;

public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Find views in the fragment layout
        ImageButton addLocationButton = rootView.findViewById(R.id.add_location_button);
        ImageButton closeModal = rootView.findViewById(R.id.close_button);
        FrameLayout mapModalLayout = rootView.findViewById(R.id.modal_layout);
        Button mapPostButton = rootView.findViewById(R.id.map_post_button);

        // Set initial visibility of map modal layout
        mapModalLayout.setVisibility(View.GONE);

        // Set OnClickListener for addLocationButton
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal layout
                if (mapModalLayout.getVisibility() == View.GONE) {
                    mapModalLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set OnClickListener for closeModal
        closeModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal layout
                if (mapModalLayout.getVisibility() == View.VISIBLE) {
                    mapModalLayout.setVisibility(View.GONE);
                }
            }
        });

        // Set OnClickListener for mapPostButton
        mapPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal layout
                if (mapModalLayout.getVisibility() == View.VISIBLE) {
                    mapModalLayout.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }
}