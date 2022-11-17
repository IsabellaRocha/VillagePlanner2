package com.example.villageplanner2;

public class UserLatLng {
    private Double latitude;
    private Double longitude;

    public UserLatLng() {}

    public UserLatLng(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
