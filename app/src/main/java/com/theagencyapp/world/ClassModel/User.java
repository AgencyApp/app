package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 04-Apr-18.
 */

public class User {

    String name;
    String phoneNo;
    String agencyid;
    String  status;

    public User(String name, String phoneNo, String agencyid, String status) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.agencyid = agencyid;
        this.status = status;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}