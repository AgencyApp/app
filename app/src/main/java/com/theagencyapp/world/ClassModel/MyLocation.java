package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 01-May-18.
 */

public class MyLocation {

    Double longitude;
    Double latitude;

    public MyLocation(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public MyLocation() {
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
