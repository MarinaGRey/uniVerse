package com.example.universe.ui.map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.universe.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.location.Location;

public class LocationDist extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Location lastLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    // Add a boolean flag to track whether the map is ready
    private boolean isMapReady = false;

    // Move the showDeviceLocation method inside the onMapReady method
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            // Enable the My Location layer and show the device's location
            showDeviceLocation();
        }
    }

    @SuppressLint("MissingPermission")
    public void showDeviceLocation() {
        if (map != null) {
            Log.e(TAG, "Map is not ready");
            map.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    lastLocation = location;
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                }
            });
        } else {
            // Map is not ready, handle appropriately (e.g., show a message or retry later)
            Log.e(TAG, "Map is not ready");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // onRequestPermissionsResult and other methods remain unchanged
}
