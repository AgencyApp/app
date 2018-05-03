package com.theagencyapp.world.ClassModel;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by hamza on 01-May-18.
 */

public class AttendanceDisplay extends Attendance {

    String employeeName;

    public AttendanceDisplay(Long timeStamp, String employeeId, String employeeName) {
        super(timeStamp, employeeId);
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
