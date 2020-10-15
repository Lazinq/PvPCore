package me.Lazinq.PvPCore.data;

import java.util.List;
import java.util.UUID;

public class Team {

    private String teamName;
    private String teamRank;
    private Integer teamCurrentSize;
    private Integer teamMaxSize;

    public Team(String teamName, String teamRank, Integer teamCurrentSize, Integer teamMaxSize) {
        this.teamName = teamName;
        this.teamRank = teamRank;
        this.teamCurrentSize = teamCurrentSize;
        this.teamMaxSize = teamMaxSize;
    }

    public String getTeamName() {
        return teamName;
    }
    public String getTeamRank() {
        return teamRank;
    }
    public Integer getTeamCurrentSize() {
        return teamCurrentSize;
    }
    public Integer getTeamMaxSize() {
        return teamMaxSize;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void setTeamRank(String teamRank) {
        this.teamRank = teamRank;
    }
    public void setTeamCurrentSize(Integer teamCurrentSize) {
        this.teamCurrentSize = teamCurrentSize;
    }
    public void setTeamMaxSize(Integer teamMaxSize) {
        this.teamMaxSize = teamMaxSize;
    }
}
