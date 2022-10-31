package com.example.villageplanner2;
import java.util.List;
import java.util.Queue;

public class DestinationActivity {
    private int ID;
    private float[] coordinates;
    private String name;
    private List<UserActivity> queue;
    private int numInQueue;
    private long queueTime;

    public String getName() {
        return name;
    }
    public List<UserActivity> getQueue(){
        return queue;
    }
    public void calculateQueueTime() {}
    public long getQueueTime() {
        return queueTime;
    }
    public int getDestinationID() {
        return 0;
    }
}