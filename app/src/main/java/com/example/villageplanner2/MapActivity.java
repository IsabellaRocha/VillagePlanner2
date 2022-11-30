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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        if (mAuth.getCurrentUser() != null) {
            UserID = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(MapActivity.this, "You must be logged in to access this page.", Toast.LENGTH_LONG).show();
            Intent mapIntent = new Intent(MapActivity.this, LandingActivity.class);
            startActivity(mapIntent);
        }
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
            Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(point).title(destinations.get(i).getStore()));
            ArrayList<String> numAndMenu = new ArrayList<String>();
            numAndMenu.add(destinations.get(i).getNumber());
            numAndMenu.add(destinations.get(i).getOrder());
            newMarker.setTag(numAndMenu);
        }



        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick (Marker marker) {
                LinearLayout display = findViewById(R.id.numAndMenu);
                display.removeAllViews();
                TextView textView = new TextView(MapActivity.this);
                textView.setClickable(true);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                ArrayList<String> numAndMenuToDisplay = (ArrayList<String>) marker.getTag();
                String text = "<a href='" + numAndMenuToDisplay.get(1) + "'> " + marker.getTitle() + "</a>";
                textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));

                TextView number = new TextView(MapActivity.this);
                number.setText(numAndMenuToDisplay.get(0));

                display.addView(textView);
                display.addView(number);
                if (polyLineList.size() > 0) {
                    mGoogleMap.clear();
                    for(int i = 0; i < destinations.size(); i++){
                        LatLng point = new LatLng(destinations.get(i).getLat(), destinations.get(i).getLng());
                        Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(point).title(destinations.get(i).getStore()));
                        ArrayList<String> numAndMenu = new ArrayList<String>();
                        numAndMenu.add(destinations.get(i).getNumber());
                        numAndMenu.add(destinations.get(i).getOrder());
                        newMarker.setTag(numAndMenu);
                    }
                }

                String markerName = marker.getTitle();
                targetLoc = marker.getPosition();
                String url = getDirectionsUrl(curLoc, targetLoc);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

                Toast.makeText(MapActivity.this, "Clicked location is " + markerName + "\nWait: " + getQueueTime(targetLoc) + " minutes", Toast.LENGTH_LONG).show();

                return false;
            }
        });
    }

    public int getQueueTime(LatLng desiredLoc) {
        int numInLine = 0;
        for (int idx = 0; idx < users.size(); idx++) {
            com.google.android.gms.maps.model.LatLng mapsLatLng =
                    new com.google.android.gms.maps.model.LatLng(users.get(idx).getLocation().getLatitude(),
                            users.get(idx).getLocation().getLongitude());
            if(mapsLatLng.equals(desiredLoc)) {
                numInLine++;
            }
        }
        int queuetime = numInLine*5;
        return queuetime;
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
                            root.getReference("users").child(UserID).child("location").setValue(latLng);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
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

    public void NavigateToDisplayProfile(View view) {
        Intent displayProfileIntent = new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(displayProfileIntent);
    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logout successful!", Toast.LENGTH_LONG).show();
        Intent remindersIntent = new Intent(getApplicationContext(), LandingActivity.class);
        startActivity(remindersIntent);
    }

    public void storeInfo(){
        Destination p1 = new Destination("Cava",34.02509105209718, -118.28452043192465, "https://www.ubereats.com/brand/cava", "2132129838");
        destinations.add(p1);

        Destination p2 = new Destination("Insomnia Cookies", 34.02502880426814, -118.28534289035802, "https://www.grubhub.com/restaurant/insomnia-cookies-929-w-jefferson-blvd-los-angeles/3167475", "2132620434");
        destinations.add(p2);

        Destination p3 = new Destination("Chinese Street Food", 34.02460072922757, -118.2840309620744, "https://www.doordash.com/store/chinese-street-food-los-angeles-2334200/", "2135365161");
        destinations.add(p3);

        Destination p4 = new Destination("City Tacos", 34.0242002463869, -118.28462466449264, "https://www.toasttab.com/city-tacos-la-835-w-jefferson-blvd-suite-1735/v3", "2135365203");
        destinations.add(p4);

        Destination p5 = new Destination("Cafe Dulce", 34.025509629008425, -118.28556220292455, "https://www.zmenu.com/cafe-dulce-los-angeles-online-menu/", "2135365609");
        destinations.add(p5);

        Destination p6 = new Destination("Greenleaf", 34.024738299179795, -118.2852476966357, "https://www.grubhub.com/restaurant/greenleaf-kitchen-and-cocktails-929-w-jefferson-blvd-ste-1650-los-angeles/724152", "2133142180");
        destinations.add(p6);

        Destination p7 = new Destination("HoneyBird", 34.02487472280556, -118.28451183795357, "https://www.grubhub.com/restaurant/honeybird-3201-s-hoover-st-los-angeles/2843207", "2135365790");
        destinations.add(p7);

        Destination p8 = new Destination("Il Giardino", 34.02522997239766, -118.28436170636583, "https://www.grubhub.com/restaurant/il-giardino-ristorante-3201-s-hoover-st-los-angeles/2159905", "2135365773");
        destinations.add(p8);

        Destination p9 = new Destination("Kobunga Korean Grill", 34.024550, -118.285630, "https://www.grubhub.com/restaurant/kobunga-929-w-jefferson-blvd-los-angeles/2072514?=undefined&utm_source=google&utm_medium=cpc&utm_campaign=&utm_term=f%3Aaggregator_serp%3Afeature_id_fprint%3D3346941308294401851&utm_content=acct_id-3075806372%3Acamp_id-13458554394%3Aadgroup_id-125754513039%3Akwd-1020510017705%3Acreative_id-541975533282%3Aext_id-%3Amatchtype_id-%3Anetwork-g%3Adevice-c%3Aloc_interest-9073456%3Aloc_physical-9073456&gclid=Cj0KCQiAm5ycBhCXARIsAPldzoWolt43IeTOf9ZWg-FzTz7Y7qaOcXztHuJ0W3dGtG1FShwDmEpFRnUaAmXaEALw_wcB&gclsrc=aw.ds", "2135365886");
        destinations.add(p9);

        Destination p10 = new Destination("Ramen Kenjo", 34.024841222085286, -118.28560863622855, "https://www.grubhub.com/restaurant/ramen-kenjo-929-w-jefferson-blvd-los-angeles/2091922", "2135365922");
        destinations.add(p10);

        Destination p11 = new Destination("Rock & Reilly's", 34.0242612207765, -118.28420760939493, "https://www.grubhub.com/restaurant/rock--reillys-usc-village-3201-s-hoover-st-ste-1810-los-angeles/1053715", "2135365584");
        destinations.add(p11);

        Destination p12 = new Destination("The Sammiche Shoppe", 34.024843260346785, -118.28413445207798, "https://www.grubhub.com/restaurant/the-sammiche-shoppe-3201-s-hoover-st-los-angeles/4760472", "2135365411");
        destinations.add(p12);

        Destination p13 = new Destination("Starbucks", 34.025095105806514, -118.2840201269872, "https://www.starbucks.com/ways-to-order/", "2134601662");
        destinations.add(p13);

        Destination p14 = new Destination("Stout Burger", 34.024786259294224, -118.28470557210025, "https://www.grubhub.com/restaurant/stout-burgers--beers-11262-ventura-blvd-studio-city/1192846", "2135365058");
        destinations.add(p14);

        Destination p15 = new Destination("Sunlife Organics", 34.02455900049386, -118.2853893524264, "https://postmates.com/store/sunlife-organics-usc-village/BXhbwXOCSpGxyJFNFVfzTA", "2135365659");
        destinations.add(p15);

        Destination p16 = new Destination("Target", 34.02601553470601, -118.2841933874132, "https://www.target.com/?ref=tgt_adv_XS000000&AFID=google&fndsrc=tgtao&DFA=71700000012637812&CPNG=Other_Target%2BBrand%7CHoliday_Ecomm_Other&adgroup=Core+Branded+Keywords&LID=700000001170706&LNM=target&MT=e&network=g&device=" +
                "c&location=9073456&targetid=aud-1879280307816:kwd-19131461&gclid=Cj0KCQiAm5ycBhCXARIsAPldzoU5UkzQiAkvnf8ImrmH1DZgiQkNK9xAj-QVpnTBBQ2fihjJ-8QxN7IaAoCbEALw_wcB&gclsrc=aw.ds", "2132753149");
        destinations.add(p16);

        Destination p17 = new Destination("Trader Joe's",34.02609027051939, -118.28466969819833, "https://www.traderjoes.com/home", "2137491497");
        destinations.add(p17);

        Destination p18 = new Destination("Amazon", 34.025699343998674, -118.28542115943962, "https://www.amazon.com/dp/B072MFYGSS/ref=as_sl_pc_as_ss_li_til?tag=deannanorris-20&linkCode=w00&linkId=a112aed00b92fa68ea132fe240f0430e&creativeASIN=B072MFYGSS&gclid=Cj0KCQi" +
                "Am5ycBhCXARIsAPldzoV59vke7EdZI0QKqEGxffZaFB_sy-04GPLYGvv9CIuWRO71o0qDeFMaAmmaEALw_wcB", "1882804331");
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

