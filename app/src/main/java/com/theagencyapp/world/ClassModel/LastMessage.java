package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 23-Apr-18.
 */

public class LastMessage {

    String lastMessage;
    String timeStamp;
    String reciverName;
    String chatContainer;
    String status;
    boolean isMap;

    public LastMessage(String lastMessage, String timeStamp, String reciverName, String chatContainer, String status, boolean isMap) {
        this.lastMessage = lastMessage;
        this.timeStamp = timeStamp;
        this.reciverName = reciverName;
        this.chatContainer = chatContainer;
        this.status = status;
        this.isMap = isMap;
    }

    public LastMessage(String reciverName, String chatContainer) {
        this.reciverName = reciverName;
        this.chatContainer = chatContainer;
    }

    public LastMessage() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getChatContainer() {
        return chatContainer;
    }

    public void setChatContainer(String chatContainer) {
        this.chatContainer = chatContainer;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean map) {
        isMap = map;
    }
}
