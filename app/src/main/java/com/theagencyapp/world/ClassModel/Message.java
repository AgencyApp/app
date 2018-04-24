package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 23-Apr-18.
 */

public class Message {
    String message;
    String timeStamp;
    boolean isMap;

    public Message(String message, String timeStamp, boolean isMap) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.isMap = isMap;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean map) {
        isMap = map;
    }
}
