package com.example.villageplanner2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ReminderActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private int ID;
    private long time;
    private boolean active;
    private int destinationID;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mAuth = FirebaseAuth.getInstance();
    }

    public void NavigateToMapsActivity(View view) {
        Intent mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(mapsIntent);
    }

    public void NavigateToRemindersActivity(View view) {
        Intent remindersIntent = new Intent(getApplicationContext(), ReminderActivity.class);
        startActivity(remindersIntent);
    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent remindersIntent = new Intent(getApplicationContext(), LandingActivity.class);
        startActivity(remindersIntent);
    }

    public long calculateTime() {
        return time;
    }
    public void setReminder() {}
    public void cancelReminder(){}
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getDestinationID() {
        return destinationID;
    }
    public void setDestinationID(int destinationID) {
        this.destinationID = destinationID;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}