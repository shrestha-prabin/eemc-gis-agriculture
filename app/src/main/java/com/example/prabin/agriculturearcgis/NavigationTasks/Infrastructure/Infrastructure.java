package com.example.prabin.agriculturearcgis.NavigationTasks.Infrastructure;

/**
 * Created by Prabin on 7/31/2018.
 */

public class Infrastructure {

    String name;
    double longitude;
    double latitude;

    public Infrastructure(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
