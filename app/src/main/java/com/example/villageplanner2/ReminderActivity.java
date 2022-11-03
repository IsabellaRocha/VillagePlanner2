package com.example.villageplanner2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;

    Button setReminder;

    private int ID;
    private long time;
    private boolean active;
    private int destinationID;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        } else {
            /*Toast.makeText(ReminderActivity.this, "You must be logged in to access this page.", Toast.LENGTH_LONG).show();
            Intent mapIntent = new Intent(ReminderActivity.this, LandingActivity.class);
            startActivity(mapIntent);

             */
        }


        setReminder = findViewById(R.id.setReminder);

        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReminderDialog();
            }
        });
    }

    void showReminderDialog() {
        final Dialog dialog = new Dialog(ReminderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.activity_setreminder);

        Spinner spinner = (Spinner) dialog.findViewById(R.id.store);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.available_stores, android.R.layout.simple_spinner_item);
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

                SimpleDateFormat currTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String totalTimeString = currDateFormat.format(currDate) + " " + timePickedString;
                try {
                    Date currTime = currTimeFormat.parse(totalTimeString);
                    time = currTime.getTime();

                    System.out.println(currTime);
                    System.out.println(time);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(totalTimeString);
            }
        });
        dialog.show();
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
        Toast.makeText(getApplicationContext(), "Logout successful!", Toast.LENGTH_LONG).show();
        Intent remindersIntent = new Intent(getApplicationContext(), LandingActivity.class);
        startActivity(remindersIntent);
    }

    public long calculateTime() {
        return time;
    }
    public void setReminder() {

    }
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
}