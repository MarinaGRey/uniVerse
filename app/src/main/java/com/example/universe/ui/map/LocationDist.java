package com.example.universe.ui.map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


// In your main activity class

// ... other imports

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

public class LocationDist extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private Location lastLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Check for location permission
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(this);
        showDeviceLocation();

    }

    private void showDeviceLocation() {

        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE);
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);


        }

        map.setMyLocationEnabled(true);
        // Get location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                // Move camera to current location
                lastLocation = location;
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                placeMarkerOnMap(currentLatLng);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f));
            }
        });
    }

    private void placeMarkerOnMap(LatLng currentLatLng){
        MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng);
        markerOptions.title("$currentLatLng");
        map.addMarker(markerOptions);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }


}
