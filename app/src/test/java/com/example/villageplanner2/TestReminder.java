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
        Assert.assertEquals("Test getDestination", destinationName, testReminder.getDestination());
    }
    @Test
    public void TestGetTime() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals("Test getTime", timeAM12, testReminder.getTime());
    }
    @Test
    public void TestGetQueueTime() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals("Test getQueueTime", queueTime, testReminder.getQueueTime());
    }
    @Test
    public void TestGetDurationTime() {
        Reminder testReminder = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals("Test getDurationTime", durationTime, testReminder.getDurationTime());
    }
    @Test
    public void TestGetTimeDisplayAM() {
        Reminder testReminderAM12 = new Reminder(destinationName, timeAM12, userID, queueTime, durationTime);
        Assert.assertEquals("Display time for 12AM", timeAM12String, testReminderAM12.getTimeDisplay());
        Reminder testReminderPM12 = new Reminder(destinationName, timePM12, userID, queueTime, durationTime);
        Assert.assertEquals("Display time for 12PM", timePM12String, testReminderPM12.getTimeDisplay());
        Reminder testReminderPast12 = new Reminder(destinationName, timePast12, userID, queueTime, durationTime);
        Assert.assertEquals("Display time for past 12PM", timePast12String, testReminderPast12.getTimeDisplay());
    }
    @Test
    public void TestEmptyReminder() {
        Reminder testReminder = new Reminder();
        Assert.assertNull("Get destination returns null", testReminder.getDestination());
        Assert.assertEquals("GetTime returns 0", 0, testReminder.getTime());
        Assert.assertEquals("GetQueueTime returns 0", 0, testReminder.getQueueTime());
        Assert.assertEquals("GetDurationTime returns 0", 0, testReminder.getDurationTime());
    }
}
