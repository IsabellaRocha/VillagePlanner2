package com.example.villageplanner2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GoogleMap mGoogleMap;
    FusedLocationProviderClient client;

    public ArrayList<Destination> destinations;
    public ArrayList<UserActivity> users;

    LatLng curLoc;
    LatLng targetLoc;
    PolylineOptions lineOptions;
    int counter;
    ArrayList<PolylineOptions> polyLineList;
    ImageView imageView;

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
        curLoc = new LatLng(0,0);
        targetLoc = new LatLng(0,0);
        lineOptions = new PolylineOptions();
        polyLineList = new ArrayList<PolylineOptions>();
        users = new ArrayList<UserActivity>();

        counter = 0;

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        this.supportMapFragment = supportMapFragment;

        destinations = new ArrayList<Destination>();
        storeInfo();

        imageView = (ImageView) findViewById(R.id.imageView);
        root.getReference("users").child(UserID).child("profilePhoto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageURL = dataSnapshot.getValue(String.class);
                Picasso.with(MapActivity.this).load(imageURL).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        root.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    System.out.println(ds);
                    UserActivity user = ds.getValue(UserActivity.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
            mGoogleMap.addMarker(new MarkerOptions().position(point).title(destinations.get(i).getStore()));
        }

        //targetLoc = new LatLng(34.025509629008425, -118.28556220292455);
//        getCurrentLocation();
//        System.out.println("targetLoc" + targetLoc);
//        System.out.println("curLoc" + curLoc);


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick (Marker marker) {
//                if(counter != 0) {
//                    lineOptions.width(0);
//                }
                int numInLine = 0;
                if (polyLineList.size() > 0) {
                    mGoogleMap.clear();
                    for(int i = 0; i < destinations.size(); i++){
                        LatLng point = new LatLng(destinations.get(i).getLat(), destinations.get(i).getLng());
                        mGoogleMap.addMarker(new MarkerOptions().position(point).title(destinations.get(i).getStore()));
                    }
                }

                String markerName = marker.getTitle();
                targetLoc = marker.getPosition();
                String url = getDirectionsUrl(curLoc, targetLoc);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

                for (int idx = 0; idx < users.size(); idx++) {
                    com.google.android.gms.maps.model.LatLng mapsLatLng =
                            new com.google.android.gms.maps.model.LatLng(users.get(idx).getLocation().getLatitude(),
                                    users.get(idx).getLocation().getLongitude());
                    System.out.println("-------------------");
                    System.out.println("UserLoc: " + mapsLatLng);
                    System.out.println("-------------------");
                    System.out.println("TargetLoc: " + targetLoc);
                    System.out.println("-------------------");

                    if(mapsLatLng.equals(targetLoc)) {
                        System.out.println("MADE IT TO QUEUE TIME");

                        numInLine++;
                    }
                }
                int queuetime = numInLine*5;
                Toast.makeText(MapActivity.this, "Clicked location is " + markerName + "\nWait: " + queuetime + " minutes", Toast.LENGTH_SHORT).show();

//                counter++;
                return false;
            }
        });
    }

    private void getCurrentLocation() {
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
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println("MADE IT");
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            curLoc = latLng;
//                            System.out.println(latLng);
//                            System.out.println("HI" + curLoc);

                            root.getReference("users").child(UserID).child("location").setValue(latLng);
                          //  MarkerOptions options = new MarkerOptions().position(latLng).title("Current location");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                          //  googleMap.addMarker(options);
//                            String url = getDirectionsUrl(curLoc, targetLoc);
//                            System.out.println(url);
//                            DownloadTask downloadTask = new DownloadTask();
//
//                            downloadTask.execute(url);
                            return;
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


    }

//  Referenced from https://www.digitalocean.com/community/tutorials/android-google-map-drawing-route-two-points

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            Map<String, String> durDist = new HashMap<>();

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                TimeJSONParser parser1 = new TimeJSONParser();

                routes = parser.parse(jObject);
                durDist = parser1.parse(jObject);
                System.out.println("Durdist is: " + durDist);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points;

//            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String)point.get("lat"));
                    double lng = Double.parseDouble((String)point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            polyLineList.add(lineOptions);

            mGoogleMap.addPolyline(lineOptions);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(curLoc);
            builder.include(targetLoc);
            LatLngBounds bound = builder.build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 300), 1500, null);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String start = "origin=" + origin.latitude + "," + origin.longitude;
        String end = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";
        String key = "&key=AIzaSyCosy6ZHbipbHHB7_-XLE7xWc_NYfF-36Q";

        String parameters = start + "&" + end + "&" + sensor + "&" + mode + "&" + key;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data ="";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

