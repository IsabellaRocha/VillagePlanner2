package com.example.villageplanner2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase root;
    String UserID;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MADE IT");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            UserID = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(EditProfileActivity.this, "You must be logged in to access this page.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditProfileActivity.this, LandingActivity.class);
            startActivity(intent);
        }
        root = FirebaseDatabase.getInstance();
        imageView = (ImageView) findViewById(R.id.imageView);
        LinearLayout profileInfo = findViewById(R.id.profileInfo);

        root.getReference("users").child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserActivity currUser = dataSnapshot.getValue(UserActivity.class);
                Picasso.with(EditProfileActivity.this).load(currUser.getProfilePhoto()).into(imageView);


                TextView firstName = new TextView(EditProfileActivity.this);
                firstName.setText("First Name: " + currUser.getFirstName());
                firstName.setTextSize(20);

                TextView lastName = new TextView(EditProfileActivity.this);
                lastName.setText("Last Name: " + currUser.getLastName());
                lastName.setTextSize(20);

                TextView email = new TextView(EditProfileActivity.this);
                email.setText("Email: " + currUser.getEmail());
                email.setTextSize(20);

                profileInfo.addView(firstName);
                profileInfo.addView(lastName);
                profileInfo.addView(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
