package com.example.universe.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.Arrays;
import java.util.List;
import com.google.android.libraries.places.api.model.Place.Field;



public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private final static int LOCATION_REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);


        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Check location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        // Initialize Places API
        String apiKey = "AIzaSyBNzMnUrmAYKYzqWqvfGRYsRrdez21yTHA";
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey);
        }
        placesClient = Places.createClient(requireContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Enable the zoom controls
        map.getUiSettings().setZoomControlsEnabled(true);

        // Check location permission again
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        // Enable the My Location layer if location permission is granted
        map.setMyLocationEnabled(true);

        // Get the last known location and move the camera to it
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            } else {
                Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch nearby places (e.g., libraries, bookshops)
        fetchNearbyPlaces();
    }

    private void fetchNearbyPlaces() {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        // Define the fields to retrieve for each nearby place
        List<Field> placeFields = Arrays.asList(Field.NAME, Field.LAT_LNG);

        // Use the Places SDK to create a FindCurrentPlaceRequest
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

        // Fetch nearby places using the PlacesClient
        placesClient.findCurrentPlace(request).addOnSuccessListener((response) -> {
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                Place place = placeLikelihood.getPlace();
                List<Place.Type> placeTypes = place.getTypes();
                if (placeTypes != null && (placeTypes.contains(Place.Type.LIBRARY) || placeTypes.contains(Place.Type.BOOK_STORE))) {
                    // Add markers for libraries and bookshops
                    LatLng placeLatLng = place.getLatLng();
                    assert placeLatLng != null;
                    map.addMarker(new MarkerOptions().position(placeLatLng).title(place.getName()));
                }
            }
        }).addOnFailureListener((exception) -> {
            // Handle any errors that occur
        });
    }

}
