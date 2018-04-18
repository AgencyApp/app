package com.theagencyapp.world.ClassModel;

import java.util.ArrayList;

/**
 * Created by hamza on 19-Apr-18.
 */

public class Team_Display extends Team {
    String teamId;

    public Team_Display(String name, ArrayList<String> employeeId, String teamId) {
        super(name, employeeId);
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
