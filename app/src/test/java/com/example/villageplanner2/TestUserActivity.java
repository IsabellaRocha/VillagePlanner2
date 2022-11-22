package com.example.villageplanner2;

import org.junit.Assert;
import org.junit.Test;

public class TestUserActivity {

    private String firstName = "Adeline";
    private String lastName = "Liou";
    private String email = "adeline@usc.edu";
    private String profilePhoto = "wwww.profilephoto.com";

    @Test
    public void TestGetFirstName() {
        UserActivity testUserActivity = new UserActivity(firstName, lastName, email, profilePhoto);
        Assert.assertEquals("Test getFirstName", firstName, testUserActivity.getFirstName());
    }

    @Test
    public void TestGetLastName() {
        UserActivity testUserActivity = new UserActivity(firstName, lastName, email, profilePhoto);
        Assert.assertEquals("Test getLastName", lastName, testUserActivity.getLastName());
    }

    @Test
    public void TestGetEmail() {
        UserActivity testUserActivity = new UserActivity(firstName, lastName, email, profilePhoto);
        Assert.assertEquals("Test getEmail", email, testUserActivity.getEmail());
    }

    @Test
    public void TestGetProfilePhoto() {
        UserActivity testUserActivity = new UserActivity(firstName, lastName, email, profilePhoto);
        Assert.assertEquals("Test profilePhoto", profilePhoto, testUserActivity.getProfilePhoto());
    }

    @Test
    public void TestSetLocation() {
        UserActivity testUserActivity = new UserActivity(firstName, lastName, email, profilePhoto);
        UserLatLng testLatLng = new UserLatLng(100.987, 9.879);
        testUserActivity.setLocation(testLatLng);
        Assert.assertEquals("Test setLocation", testLatLng, testUserActivity.getLocation());
    }

    @Test
    public void TestEmptyUser() {
        UserActivity testUserActivity = new UserActivity();
        Assert.assertNull(testUserActivity.getEmail());
        Assert.assertNull(testUserActivity.getFirstName());
        Assert.assertNull(testUserActivity.getLastName());
        Assert.assertNull(testUserActivity.getProfilePhoto());
        Assert.assertNull(testUserActivity.getLocation());
    }
}
