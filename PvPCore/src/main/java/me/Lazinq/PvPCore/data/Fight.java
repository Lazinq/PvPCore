package me.Lazinq.PvPCore.data;

import java.util.List;
import java.util.UUID;

public class Fight {

    private String team;
    private List<UUID> queuedPlayers;
    private Boolean ranked;
    private Integer currentSize;
    private Integer size;
    private String currentMap;
    private String opponentTeam;

    public Fight(String team, List<UUID> queuedPlayers, Boolean ranked, Integer currentSize, Integer size, String opponentTeam) {
        this.team = team;
        this.queuedPlayers = queuedPlayers;
        this.ranked = ranked;
        this.currentSize = currentSize;
        this.size = size;
        this.opponentTeam = opponentTeam;
    }

    public String getTeam() {
        return team;
    }
    public String getCurrentMap() {
        return currentMap;
    }
    public List<UUID> getQueuedPlayers() {
        return queuedPlayers;
    }
    public Boolean isRanked() {
        return ranked;
    }
    public Integer getMaxSize() {
        return size;
    }
    public Integer getCurrentSize() {
        return currentSize;
    }
    public String getOpponentTeam() {
        return currentMap;
    }

    public void setCurrentMap(String team) {
        this.currentMap = currentMap;
    }
    public void setQueuedPlayers(List<UUID> queuedPlayers) {
        this.queuedPlayers = queuedPlayers;
    }
    public void setCurrentSize(Integer currentSize) {
        this.currentSize = currentSize;
    }
    public void setMaxSize(Integer size) {
        this.size = size;
    }
    public void setRanked(Boolean ranked) {
        this.ranked = ranked;
    }
    public void setOpponentTeam(String opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

}