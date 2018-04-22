package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 17-Apr-18.
 */

public class MileStone {

    String startTime;
    String endtime;
    String title ;
    String descriptions;

    public MileStone(String startTime, String endtime, String title, String descriptions) {
        this.startTime = startTime;
        this.endtime = endtime;
        this.title = title;
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
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
