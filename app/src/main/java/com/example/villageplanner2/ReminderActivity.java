package com.example.villageplanner2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReminderActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase root;
    public ArrayList<Destination> destinations;
    public ArrayList<UserActivity> users;

    Button setReminder;

    private long time;
    private String destination;
    private String userID;
    private long queueTime;
    private LatLng curLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        root = FirebaseDatabase.getInstance();
        users = new ArrayList<UserActivity>();
        curLoc = new LatLng(0, 0);
        queueTime = 0;
        if (user != null) {
            userID = user.getUid();
        } else {
            Toast.makeText(ReminderActivity.this, "You must be logged in to access this page.", Toast.LENGTH_LONG).show();
            Intent mapIntent = new Intent(ReminderActivity.this, LandingActivity.class);
            startActivity(mapIntent);
        }
        createNotificationChannel();
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

        root.getReference("users").child(userID).child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserLatLng userLoc = dataSnapshot.getValue(UserLatLng.class);
                curLoc = new com.google.android.gms.maps.model.LatLng(userLoc.getLatitude(),
                        userLoc.getLongitude());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        destinations = new ArrayList<Destination>();
        storeInfo();

        LinearLayout displayReminders = findViewById(R.id.reminders);
        root.getReference("users").child(userID).child("reminders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot remind : dataSnapshot.getChildren()) {
                    Reminder reminder = remind.getValue(Reminder.class);

                    TextView storeName = new TextView(ReminderActivity.this);
                    storeName.setText(reminder.getDestination());

                    TextView timeOfReminder = new TextView(ReminderActivity.this);
                    timeOfReminder.setText("Time of reminder: " + reminder.getTimeDisplay());

                    Button cancelButton = new Button(ReminderActivity.this);
                    cancelButton.setText("Cancel Reminder");
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            remind.getRef().removeValue();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    });

                    displayReminders.addView(storeName);
                    displayReminders.addView(timeOfReminder);
                    displayReminders.addView(cancelButton);

                    Notification notification = setNotification("Head towards " + reminder.getDestination(), "Arrive by " + reminder.getTimeDisplay() +
                            ". Wait time: " + reminder.getQueueTime() / 60000 + " mins." +
                            " Walking time: " + reminder.getDurationTime()/60000 + " mins.");
                    Intent intent = new Intent(ReminderActivity.this, NotificationActivity.class);
                    intent.putExtra("notification", notification);
                    intent.putExtra("id", (int) reminder.getTime());
                    
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTime() - reminder.getQueueTime() - reminder.getDurationTime(), pendingIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setReminder = findViewById(R.id.setReminder);
        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReminderDialog();
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

    public Notification setNotification(String title, String body)
    {
        NotificationCompat.Builder build = new NotificationCompat.Builder(getApplicationContext(), "notifyVillagePlanner")
                .setSmallIcon(R.drawable.villageplanner_logo_black_transparent_background)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return build.build();
    }

    void showReminderDialog() {
        final Dialog dialog = new Dialog(ReminderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.activity_setreminder);

        Spinner spinner = (Spinner) dialog.findViewById(R.id.store);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_destinations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button confirmReminder = dialog.findViewById(R.id.confirmReminder);

        confirmReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePicker timePicked = dialog.findViewById(R.id.timePicker);
                String timePickedString = timePicked.getHour() + ":" + timePicked.getMinute();
                SimpleDateFormat currDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currDate = new Date();

                destination = spinner.getSelectedItem().toString();
                Destination desiredDestination = new Destination();
                for(int idx = 0; idx < destinations.size(); idx++) {
                    if (destinations.get(idx).getStore().equals(destination)) {
                        desiredDestination = destinations.get(idx);
                        break;
                    }
                }
                queueTime = (long) getQueueTime(desiredDestination.getLatlng()) * 60000;
                String url = getDirectionsUrl(curLoc, desiredDestination.getLatlng());


                SimpleDateFormat currTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String totalTimeString = currDateFormat.format(currDate) + " " + timePickedString;
                try {
                    Date currTime = currTimeFormat.parse(totalTimeString);
                    time = currTime.getTime();

                    Date currTimeForValidation = new Date();
                    long currTimeForValidationLong = currTimeForValidation.getTime();
                    if (currTimeForValidationLong > time) {
                        Toast.makeText(ReminderActivity.this, "This time has already passed. Set a reminder for later today.", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
                dialog.dismiss();
            }
        });
        dialog.show();
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

    public void setReminder(long durationTime) {
        Reminder reminderToAdd = new Reminder(destination, time, userID, queueTime, durationTime);
        root.getReference("users").child(userID).child("reminders").push().setValue(reminderToAdd);
    }


    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            CharSequence name = "VillagePlannerNotificationChannel";
            String description = "Channel for VillagePlanner reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyVillagePlanner", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

            String duration = durDist.get("Duration");

            long durationTime = Long.parseLong(duration.substring(0, duration.length() - 5)) * 60000;

            setReminder(durationTime);

            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        }
    }

    public void setUsers(ArrayList<UserActivity> users) {
        this.users = users;
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