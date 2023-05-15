package com.example.myapplication;

public class MyLocation {
    private double lat;
    private double longi;

    public MyLocation(double latitude, double longitude) {
        this.lat = latitude;
        this.longi = longitude;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return longi;
    }
}
