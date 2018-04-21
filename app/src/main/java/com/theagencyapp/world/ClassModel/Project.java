package com.theagencyapp.world.ClassModel;

/**
 * Created by hamza on 17-Apr-18.
 */

public class Project {

    String name;
    String mileStoneContainer;
    String clientId;
    String teamId;
    String priority;
    String deadline;
    String description;

    public Project(String name, String mileStoneContainer, String clientId, String teamId, String priority, String deadline, String description) {
        this.name = name;
        this.mileStoneContainer = mileStoneContainer;
        this.clientId = clientId;
        this.teamId = teamId;
        this.priority = priority;
        this.deadline = deadline;
        this.description = description;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
