package com.theagencyapp.world.ClassModel;

import java.util.ArrayList;

/**
 * Created by abdul on 4/26/2018.
 */

public class User_Display extends User {
    String userId;
    String imageUrl;

    public User_Display(String name, String phoneNo, String agencyid, String status, String uId, String imageUrl) {
        super(name, phoneNo, agencyid, status);
        this.userId = uId;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
