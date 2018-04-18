package com.theagencyapp.world.ClassModel;

import java.util.ArrayList;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Employee {
    ArrayList<String> skills;
    String imageUrl;

    public Employee(ArrayList<String> skills, String imageUrl) {
        this.skills = skills;
        this.imageUrl = imageUrl;
    }

    public Employee() {
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
