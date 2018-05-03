package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 01-May-18.
 */

public class Attendance {
    Long timeStamp;
    String employeeId;

    public Attendance(Long timeStamp, String employeeId) {
        this.timeStamp = timeStamp;
        this.employeeId = employeeId;
    }

    public Attendance() {
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
