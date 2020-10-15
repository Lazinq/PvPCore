package me.Lazinq.PvPCore.commands;

import me.Lazinq.PvPCore.PvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Arena implements CommandExecutor {

    private PvP plugin;

    public Arena(PvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (label.equalsIgnoreCase("Arena")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length==1) {
                        player.sendMessage("Format: /Arena create [Name] [World] [x] [y] [z] [x] [y] [z]");
                    }
                    List<Location> locations = new ArrayList<>();
                    
                    World world = Bukkit.getServer().getWorld(args[2]);
                    Integer coordsx = Integer.parseInt(args[3]);
                    Integer coordsy = Integer.parseInt(args[4]);
                    Integer coordsz = Integer.parseInt(args[5]);
                    locations.add(new Location(world, coordsx, coordsy, coordsz));
                    Integer coordsx1 = Integer.parseInt(args[6]);
                    Integer coordsy1 = Integer.parseInt(args[7]);
                    Integer coordsz1 = Integer.parseInt(args[8]);
                    locations.add(new Location(world, coordsx1, coordsy1, coordsz1));
                    this.plugin.getChunkLocations().put(args[1], locations);
                }
                if (args[0].equalsIgnoreCase("setspawnlocations")) {
                    if (args.length==1) {
                        player.sendMessage("Format: /Arena setspawnlocations [Name] [World] [x] [y] [z] [x] [y] [z]");
                    }
                    List<Location> locations = new ArrayList<>();

                    World world = Bukkit.getServer().getWorld(args[2]);
                    Integer coordsx = Integer.parseInt(args[3]);
                    Integer coordsy = Integer.parseInt(args[4]);
                    Integer coordsz = Integer.parseInt(args[5]);
                    locations.add(new Location(world, coordsx, coordsy, coordsz));
                    Integer coordsx1 = Integer.parseInt(args[6]);
                    Integer coordsy1 = Integer.parseInt(args[7]);
                    Integer coordsz1 = Integer.parseInt(args[8]);
                    locations.add(new Location(world, coordsx1, coordsy1, coordsz1));
                    this.plugin.getSpawnLocations().put(args[1], locations);
                }
                if (args[0].equalsIgnoreCase("list")) {
                    for (Map.Entry<String, List<Location>> map : this.plugin.getChunkLocations().entrySet()) {
                        player.sendMessage(map.getKey().split(", "));
                    }
                }
            }
        }
        return false;
    }
}
