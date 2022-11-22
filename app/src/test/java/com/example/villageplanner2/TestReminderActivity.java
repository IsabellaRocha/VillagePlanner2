/*package com.example.villageplanner2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TestReminderActivity {
    private UserLatLng userLatLng = new UserLatLng(34.02601553470601, 118.2841933874132);
    private LatLng latlng = new LatLng(34.02601553470601, 118.2841933874132);

    @Test
    public void TestGetQueueTime() {
        ReminderActivity testReminderActivity = new ReminderActivity();

        UserActivity user1 = new UserActivity("Izzy", "Rocha", "irocha@usc.edu", "url.com");
        UserActivity user2 = new UserActivity("Adeline", "Liou", "a@usc.edu", "url.com");
        user1.setLocation(userLatLng);
        user2.setLocation(userLatLng);
        ArrayList<UserActivity> users = new ArrayList<UserActivity>();
        users.add(user1);
        users.add(user2);

        testReminderActivity.setUsers(users);

        Assert.assertEquals(10, testReminderActivity.getQueueTime(latlng));
    }
}

 */
