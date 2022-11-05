package com.example.villageplanner2;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.R.layout;
import android.R.id;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    FirebaseAuth mAuth;
    FirebaseDatabase root;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth= FirebaseAuth.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        root = FirebaseDatabase.getInstance();

        //mapFragment = getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapAPI = googleMap;
        LatLng USCVillage = new LatLng(34.0256, -118.2850);
        mapAPI.addMarker(new MarkerOptions().position(USCVillage).title("USCVillage"));
        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(USCVillage, 17.0f));

        //getLocationPermission();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        System.out.println(locationPermissionGranted == true);
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = mfusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    System.out.println(task.isSuccessful() == false);
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location currentLocation = task.getResult();
//                        assert currentLocation == null : "null";
                        System.out.println(currentLocation == null);
                        if (currentLocation != null) {
                            LatLng current = new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude());
                            root.getReference("users").child(UserID).child("location").push().setValue(current);
                            mapAPI.addMarker(new MarkerOptions().position(current).title("Current Location"));
                            mapAPI.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    current, 15));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}