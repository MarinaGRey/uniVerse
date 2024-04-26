package com.example.universe.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.universe.R;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

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

        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

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


    @Override
    public void onMapReady(GoogleMap map) {
        map.setOnPoiClickListener(this);

        float zoomLevel = map.getMaxZoomLevel() - 5;
        LatLng uc3m = new LatLng(40.332690215919904, -3.765110001822394); // UC3M Leganés
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(uc3m, zoomLevel));
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest poi) {
        Toast.makeText(getActivity(), "Clicked: " + poi.name + "\nPlace ID:" + poi.placeId + "\nLatitude:" +
                        poi.latLng.latitude + " Longitude:" + poi.latLng.longitude, Toast.LENGTH_SHORT)
                .show();


    }
}