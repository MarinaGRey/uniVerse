package com.example.universedefinitivo.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.universedefinitivo.R;
import com.example.universedefinitivo.ui.home.HomeActivity;
import com.example.universedefinitivo.ui.profile.YourPostsActivity;

public class MapActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        ImageButton addLocationButton = findViewById(R.id.add_location_button);
        ImageButton closeModal = findViewById(R.id.close_button);
        FrameLayout mapModalLayout = findViewById(R.id.modal_layout);
        mapModalLayout.setVisibility(View.GONE);
        Button mapPostButton = findViewById(R.id.map_post_button);

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal view
                if (mapModalLayout.getVisibility() == View.GONE) {
                    mapModalLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        closeModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal view
                if (mapModalLayout.getVisibility() == View.VISIBLE) {
                    mapModalLayout.setVisibility(View.GONE);
                }
            }
        });
        mapPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of map modal view
                if (mapModalLayout.getVisibility() == View.VISIBLE) {
                    mapModalLayout.setVisibility(View.GONE);
                }
            }
        });

    }
}



