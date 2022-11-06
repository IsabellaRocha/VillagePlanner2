package com.example.villageplanner2;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class Destination {
    private String store;
    LatLng latlng;
    double lat;
    double lng;
    double waitTime;

    public Destination(){

    }

    public Destination(String store, double lat, double lng){
        this.store = store;
        this.lat = lat;
        this.lng = lng;
    }

    public Destination(String store){
        this.store = store;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getStore() {
        return this.store;
    }

    public double getWaitTime() {
        return waitTime;
    }

}
