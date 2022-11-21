package com.example.villageplanner2;

public class UserActivity {

    private String firstName;
    private String lastName;
    private String email;
    private String profilePhoto;
    private UserLatLng location;

    public UserActivity(){}

    public UserActivity(String firstName, String lastName, String email, String profilePhoto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhoto = profilePhoto;
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
    public UserLatLng getLocation() {return location;}

    public void setLocation(UserLatLng location) {
        this.location = location;
    }
}
