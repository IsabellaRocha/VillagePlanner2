package com.example.villageplanner2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase root;

    Button setReminder;

    private long time;
    private String destination;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        root = FirebaseDatabase.getInstance();
        if (user != null) {
            userID = user.getUid();
        } else {
            /*Toast.makeText(ReminderActivity.this, "You must be logged in to access this page.", Toast.LENGTH_LONG).show();
            Intent mapIntent = new Intent(ReminderActivity.this, LandingActivity.class);
            startActivity(mapIntent);

             */
        }
        createNotificationChannel();

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

                    Notification notification = setNotification("Start heading towards " + reminder.getDestination(), "You will arrive by " + reminder.getTimeDisplay());
                    Intent intent = new Intent(ReminderActivity.this, NotificationActivity.class);
                    intent.putExtra("notification", notification);
                    intent.putExtra("id", (int) reminder.getTime());
                    
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTime(), pendingIntent);
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

    public Notification setNotification(String title, String body)
    {
        NotificationCompat.Builder build = new NotificationCompat.Builder(getApplicationContext(), "notifyVillagePlanner")
                .setSmallIcon(R.drawable.villageplanner_logo_black_transparent_background)
                .setContentTitle(title)
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

                destination = spinner.getSelectedItem().toString();

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
                setReminder();
                dialog.dismiss();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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

    public void setReminder() {
        Reminder reminderToAdd = new Reminder(destination, time, userID);
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
}