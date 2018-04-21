package com.theagencyapp.world.ClassModel;

import java.util.ArrayList;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Team {
    String name;
    ArrayList<String>employeeId;

    public Team(String name, ArrayList<String> employeeId) {
        this.name = name;
        this.employeeId = employeeId;
    }

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(ArrayList<String> employeeId) {
        this.employeeId = employeeId;
    }

}
