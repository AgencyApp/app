package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 23-Apr-18.
 */

public class Message {
    String message;
    Long timeStamp;
    String senderUid;
    String reciverUid;
    boolean isMap;

    public Message(String message, Long timeStamp, String senderUid, String reciverUid, boolean isMap) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.senderUid = senderUid;
        this.reciverUid = reciverUid;
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

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean map) {
        isMap = map;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReciverUid() {
        return reciverUid;
    }

    public void setReciverUid(String reciverUid) {
        this.reciverUid = reciverUid;
    }
}
