package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 17-Apr-18.
 */

public class MileStone {

    String startTime;
    String endtime;
    String name;
    String descriptions;

    public MileStone(String startTime, String endtime, String name, String descriptions) {
        this.startTime = startTime;
        this.endtime = endtime;
        this.name = name;
        this.descriptions = descriptions;
    }

    public MileStone() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
