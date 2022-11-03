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
        String AMOrPM = "AM";
        SimpleDateFormat currTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currTime = new Date(time);
        String currTimeString = currTimeFormat.format(currTime);
        int hour = Integer.parseInt(currTimeString.substring(11, 13));
        int min = Integer.parseInt(currTimeString.substring(14, 16));
        if (hour > 12) {
            AMOrPM = "PM";
            hour -= 12;
        }
        else if(hour == 0) {
            hour = 12;
        }
        else if(hour == 12) {
            AMOrPM = "PM";
        }
        String hourAndMin = String.format("%02d:%02d %s", hour, min, AMOrPM);
        return hourAndMin;
    }
    public long getTime() {
        return time;
    }
}
