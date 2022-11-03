package com.example.villageplanner2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reminder {
    private String destination;
    private long time;
    private String userID;

    public Reminder(){}

    public Reminder(String destination, long time, String userID) {
        this.destination = destination;
        this.time = time;
        this.userID = userID;
    }

    public String getDestination() {
        return destination;
    }
    public String getTimeDisplay() {
        SimpleDateFormat currTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currTime = new Date(time);
        String currTimeString = currTimeFormat.format(currTime);
        String hourAndMin = currTimeString.substring(11, 16);
        return hourAndMin;
    }
    public long getTime() {
        return time;
    }
}
