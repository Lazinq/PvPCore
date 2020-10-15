package me.Lazinq.PvPCore;

import me.Lazinq.PvPCore.commands.Arena;
import me.Lazinq.PvPCore.commands.Teams;
import me.Lazinq.PvPCore.data.Team;
import me.Lazinq.PvPCore.data.Fight;
import me.Lazinq.PvPCore.objects.Elo;
import me.Lazinq.PvPCore.objects.QueueSystem;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PvP extends JavaPlugin {

    public void onEnable() {
        this.getCommand("Arena").setExecutor(new Arena(this));
        this.getCommand("Team").setExecutor(new Teams(this));
        getServer().getPluginManager().registerEvents(new Elo(this), this);
        getServer().getPluginManager().registerEvents(new QueueSystem(this), this);
    }

    //UUIDs, TeamInfo
    private final HashMap<UUID, Team> teamInfo = new HashMap<>();
    //UUID, UUID Inviter
    private final HashMap<UUID, UUID> pendingInvites = new HashMap<>();
    //UUID, TeamName
    private final HashMap<UUID, Long> pendingRemoval = new HashMap<>();
    //TeamName, Data
    private final HashMap<UUID, Fight> teamWarriors = new HashMap<>();
    //MapName, List of Teams
    private final HashMap<String, List<UUID>> currentPlayers = new HashMap<>();
    //TeamName, Elo
    private final HashMap<String, Double> teamElo = new HashMap<>();
    //MapName, List of Teams
    private final HashMap<String, List<String>> usedMaps = new HashMap<>();
    //MapName, List of Players in the game
    private final HashMap<Boolean, List<UUID>> currentFights = new HashMap<>();
    //ArenaName Locations
    private final HashMap<String, List<Location>> chunkLocations = new HashMap<>();
    //ArenaName Locations
    private final HashMap<String, List<Location>> spawnLocation = new HashMap<>();

    public Map<UUID, Team> getTeamInfo() {
        return teamInfo;
    }
    public Map<UUID, UUID> getPendingInvites() {
        return pendingInvites;
    }
    public Map<UUID, Long> getPendingRemoval() {
        return pendingRemoval;
    }
    public Map<UUID, Fight> getTeamWarriors() {
        return teamWarriors;
    }
    public Map<String, List<UUID>> getCurrentPlayers() {
        return currentPlayers;
    }
    public Map<String, Double> getTeamElo() {
        return teamElo;
    }
    public Map<String, List<String>> getUsedMaps() {
        return usedMaps;
    }
    public Map<Boolean, List<UUID>> getCurrentFights() {
        return currentFights;
    }
    public Map<String, List<Location>> getChunkLocations() { return chunkLocations; }
    public Map<String, List<Location>> getSpawnLocations() { return spawnLocation; }

}
