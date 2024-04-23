package com.example.universe.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.universe.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, set up map
            setUpMap(rootView);
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        return rootView;
    }

    private void setUpMap(View rootView) {
        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Find views in the fragment layout
        ImageButton addLocationButton = rootView.findViewById(R.id.add_location_button);
        ImageButton closeModal = rootView.findViewById(R.id.close_button);
        FrameLayout mapModalLayout = rootView.findViewById(R.id.modal_layout);
        Button mapPostButton = rootView.findViewById(R.id.map_post_button);

        // Set initial visibility of map modal layout
        mapModalLayout.setVisibility(View.GONE);

        // Set OnClickListener for addLocationButton
        addLocationButton.setOnClickListener(v -> {
            // Toggle visibility of map modal layout
            if (mapModalLayout.getVisibility() == View.GONE) {
                mapModalLayout.setVisibility(View.VISIBLE);
            }
        });

        // Set OnClickListener for closeModal
        closeModal.setOnClickListener(v -> {
            // Toggle visibility of map modal layout
            if (mapModalLayout.getVisibility() == View.VISIBLE) {
                mapModalLayout.setVisibility(View.GONE);
            }
        });

        // Set OnClickListener for mapPostButton
        mapPostButton.setOnClickListener(v -> {
            // Toggle visibility of map modal layout
            if (mapModalLayout.getVisibility() == View.VISIBLE) {
                mapModalLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable the My Location layer if permission has been granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            // Move camera to current location
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set up map
                setUpMap(getView());
            }
        }
    }
}
