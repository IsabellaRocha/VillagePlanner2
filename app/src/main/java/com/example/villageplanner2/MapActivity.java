package com.example.villageplanner2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GoogleMap mGoogleMap;
    FusedLocationProviderClient client;

    public ArrayList<Destination> destinations;

    FirebaseAuth mAuth;
    FirebaseDatabase root;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        root = FirebaseDatabase.getInstance();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        this.supportMapFragment = supportMapFragment;

        destinations = new ArrayList<Destination>();
        storeInfo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        mGoogleMap.setMyLocationEnabled(true);

        for(int i = 0; i < destinations.size(); i++){
            LatLng point = new LatLng(destinations.get(i).getLat(), destinations.get(i).getLng());
            mGoogleMap.addMarker(new MarkerOptions().position(point));
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("No Permission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        System.out.println("Yes Permission");
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println("MADE IT");
                System.out.println(location);
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            System.out.println(latLng);
                            root.getReference("users").child(UserID).child("location").setValue(latLng);
                          //  MarkerOptions options = new MarkerOptions().position(latLng).title("Current location");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                          //  googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    public void NavigateToMapsActivity(View view) {
        Intent mapsIntent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(mapsIntent);
    }

    public void NavigateToRemindersActivity(View view) {
        Intent remindersIntent = new Intent(getApplicationContext(), ReminderActivity.class);
        startActivity(remindersIntent);
    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logout successful!", Toast.LENGTH_LONG).show();
        Intent remindersIntent = new Intent(getApplicationContext(), LandingActivity.class);
        startActivity(remindersIntent);
    }

    public void storeInfo(){
        Destination p1 = new Destination("Cava",34.02509105209718, -118.28452043192465);
        destinations.add(p1);

        Destination p2 = new Destination("Insomnia Cookies", 34.02502880426814, -118.28534289035802);
        destinations.add(p2);

        Destination p3 = new Destination("Chinese Street Food", 34.02460072922757, -118.2840309620744);
        destinations.add(p3);

        Destination p4 = new Destination("City Tacos", 34.0242002463869, -118.28462466449264);
        destinations.add(p4);

        Destination p5 = new Destination("Cafe Dulce", 34.025509629008425, -118.28556220292455);
        destinations.add(p5);

        Destination p6 = new Destination("Greenleaf", 34.024738299179795, -118.2852476966357);
        destinations.add(p6);

        Destination p7 = new Destination("HoneyBird", 34.02487472280556, -118.28451183795357);
        destinations.add(p7);

        Destination p8 = new Destination("Il Giardino", 34.02522997239766, -118.28436170636583);
        destinations.add(p8);

        Destination p9 = new Destination("Kobunga Korean Grill", 34.024550, -118.285630);
        destinations.add(p9);

        Destination p10 = new Destination("Ramen Kenjo", 34.024841222085286, -118.28560863622855);
        destinations.add(p10);

        Destination p11 = new Destination("Rock & Reilly's", 34.0242612207765, -118.28420760939493);
        destinations.add(p11);

        Destination p12 = new Destination("The Sammiche Shoppe", 34.024843260346785, -118.28413445207798);
        destinations.add(p12);

        Destination p13 = new Destination("Starbucks", 34.025095105806514, -118.2840201269872);
        destinations.add(p13);

        Destination p14 = new Destination("Stout Burger", 34.024786259294224, -118.28470557210025);
        destinations.add(p14);

        Destination p15 = new Destination("Sunlife Organics", 34.02455900049386, -118.2853893524264);
        destinations.add(p15);

        Destination p16 = new Destination("Target", 34.02601553470601, -118.2841933874132);
        destinations.add(p16);

        Destination p17 = new Destination("Trader Joe's",34.02609027051939, -118.28466969819833);
        destinations.add(p17);

        Destination p18 = new Destination("Amazon", 34.025699343998674, -118.28542115943962);
        destinations.add(p18);

        Destination p19 = new Destination("Bank of America", 34.025235060353964, -118.28415070344676);
        destinations.add(p19);

        Destination p20 = new Destination("CorePower Yoga", 34.03171891003997, -118.28461085453011);
        destinations.add(p20);

        Destination p21 = new Destination("FedEx", 34.025384941397455, -118.2858292865064);
        destinations.add(p21);

        Destination p22 = new Destination("Fruit + Candy", 34.024403211631366, -118.28424010828822);
        destinations.add(p22);

        Destination p23 = new Destination("Kaitlyn", 34.02428117857448, -118.28483126226855);
        destinations.add(p23);

        Destination p24 = new Destination("Mac Repair Clinic", 34.024741162645796, -118.28538271349358);
        destinations.add(p24);

        Destination p26 = new Destination("Simply Nail Bar", 34.024968033993, -118.28414552883545);
        destinations.add(p26);

        Destination p27 = new Destination("Sole Bicycles", 34.0242865294595, -118.28476912152854);
        destinations.add(p27);

        Destination p28 = new Destination("USC Credit Union", 34.026399205083244, -118.28509300996214);
        destinations.add(p28);

        Destination p29 = new Destination("USC Roski Eye Institute", 34.02454927809253, -118.28450065503901);
        destinations.add(p29);

        Destination p30 = new Destination("Village Cobbler", 34.02495461803059, -118.28407221349356);
        destinations.add(p30);

        Destination p31 = new Destination("Workshop Salon + Boutique", 34.02625828327596, -118.28473960184829);
        destinations.add(p31);
    }
}

