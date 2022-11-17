package com.example.villageplanner2;

import org.junit.Assert;
import org.junit.Test;

public class TestReminder {
    private String destinationName = "target";
    private long timeAM12 = 1668672610000L;
    private String timeAM12String = "12:10 AM";
    private long timePM12 = 1668715810000L;
    private String timePM12String = "12:10 PM";
    private long timePast12 = 1668733810000L;
    private String timePast12String = "05:10 PM";
    private String userID = "userID";
    private long queueTime = 5*60000;
    private long durationTime = 12*60000;

    @Test
    public void TestGetDestination() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals(destinationName, testReminder.getDestination());
    }
    @Test
    public void TestGetTime() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals(timeAM12, testReminder.getTime());
    }
    @Test
    public void TestGetQueueTime() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals(queueTime, testReminder.getQueueTime());
    }
    @Test
    public void TestGetDurationTime() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals(durationTime, testReminder.getDurationTime());
    }
    @Test
    public void TestGetTimeDisplayAM() {
        Reminder testReminderAM12 = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals(timeAM12String, testReminderAM12.getTimeDisplay());
        Reminder testReminderPM12 = new Reminder(destinationName, timePM12, userID, queueTime, durationTime);
        Assert.assertEquals(timePM12String, testReminderPM12.getTimeDisplay());
        Reminder testReminderPast12 = new Reminder(destinationName, timePast12, userID, queueTime, durationTime);
        Assert.assertEquals(timePast12String, testReminderPast12.getTimeDisplay());
    }
}
