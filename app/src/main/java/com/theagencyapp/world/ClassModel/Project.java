package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Project {

    String name;
    String fieldType;
    String mileStoneContainer;
    String clientId;
    String teamId;
    String priority;

    public Project(String name, String fieldType, String mileStoneContainer, String clientId, String teamId, String priority) {
        this.name = name;
        this.fieldType = fieldType;
        this.mileStoneContainer = mileStoneContainer;
        this.clientId = clientId;
        this.teamId = teamId;
        this.priority = priority;
    }

    public Project() {
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getMileStoneContainer() {
        return mileStoneContainer;
    }

    public void setMileStoneContainer(String mileStoneContainer) {
        this.mileStoneContainer = mileStoneContainer;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
