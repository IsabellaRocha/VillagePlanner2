package com.example.villageplanner2;

public class ReminderActivity {
    private int ID;
    private long time;
    private boolean active;
    private int destinationID;
    private int userID;

    public long calculateTime() {
        return time;
    }
    public void setReminder() {}
    public void cancelReminder(){}
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getDestinationID() {
        return destinationID;
    }
    public void setDestinationID(int destinationID) {
        this.destinationID = destinationID;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}