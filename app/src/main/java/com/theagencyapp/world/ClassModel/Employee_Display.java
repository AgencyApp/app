package com.theagencyapp.world.ClassModel;

import java.util.ArrayList;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Employee_Display extends User {
    ArrayList<String> skills;
    String ImageUrl;
    String employee_id;

    public Employee_Display(String name, String phoneNo, String agencyid, String status, ArrayList<String> skills, String imageUrl, String employee_id) {
        super(name, phoneNo, agencyid, status);
        this.skills = skills;
        ImageUrl = imageUrl;
        this.employee_id = employee_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public Employee_Display() {
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
