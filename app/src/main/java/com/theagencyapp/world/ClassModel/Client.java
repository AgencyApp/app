package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Client {
    float ratings;
    String imageUrl;

    public Client(float ratings, String imageUrl) {
        this.ratings = ratings;
        this.imageUrl = imageUrl;
    }

    public Client() {
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
