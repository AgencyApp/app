package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Client_Display extends User {
    float ratings;
    String client_id;
    String ImageUrl;

    public Client_Display(String name, String phoneNo, String agencyid, String status, float ratings, String client_id, String imageUrl) {
        super(name, phoneNo, agencyid, status);
        this.ratings = ratings;
        this.client_id = client_id;
        ImageUrl = imageUrl;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
