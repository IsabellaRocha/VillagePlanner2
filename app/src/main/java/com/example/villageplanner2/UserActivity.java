package com.example.villageplanner2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class UserActivity {
    private String firstName;
    private String lastName;
    private String email;
    private int userID;
    private String profilePhoto;
    private float[] coordinates;
    private LatLng location;
    private List<ReminderActivity> reminders;

    public UserActivity(){}

    public UserActivity(String firstName, String lastName, String email, String profilePhoto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhoto = profilePhoto;
        reminders = new ArrayList<ReminderActivity>();
        location = new LatLng(0, 0); 
    }

    public List<ReminderActivity> getReminders() {
        return reminders;
    }
    public String getProfilePhoto() {
        return profilePhoto;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public LatLng getLocation() {return location;}
    public void setLocation(LatLng loc){location = loc;}
}
