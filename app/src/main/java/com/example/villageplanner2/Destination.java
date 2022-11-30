package com.example.villageplanner2;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class Destination {
    private String store;
    LatLng latlng;
    double lat;
    double lng;
    double waitTime;
    String order;
    String number;


    public Destination(){

    }

    public Destination(String store, double lat, double lng, String order, String number){
        this.store = store;
        this.lat = lat;
        this.lng = lng;
        this.order = order;
        this.number = number;

        latlng = new LatLng(lat, lng);
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

    public LatLng getLatlng() {
        return latlng;
    }

    public String getStore() {
        return this.store;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public String getOrder() {return order;}

    public String getNumber() {return number;}

}
