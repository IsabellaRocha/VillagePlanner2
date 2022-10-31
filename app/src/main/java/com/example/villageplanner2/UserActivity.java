package com.example.villageplanner2;

import java.util.List;

public class UserActivity {
    private String firstName;
    private String lastName;
    private String email;
    private int ID;
    private String profilePhoto;
    private float[] coordinates;
    private List<ReminderActivity> reminders;

    public void createAccount() {}
    public void logIn(){}
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
    public int getID() {
        return ID;
    }
}
