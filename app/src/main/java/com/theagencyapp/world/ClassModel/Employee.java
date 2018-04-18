package com.theagencyapp.world.ClassModel;

import java.util.ArrayList;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Employee {
    ArrayList<String> skills;
    String ImageUrl;

    public Employee(ArrayList<String> skills, String imageUrl) {
        this.skills = skills;
        ImageUrl = imageUrl;
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
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
