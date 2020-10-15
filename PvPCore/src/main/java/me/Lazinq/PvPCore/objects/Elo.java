package me.Lazinq.PvPCore.objects;

import me.Lazinq.PvPCore.PvP;
import me.Lazinq.PvPCore.data.Fight;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Elo implements Listener {

    private PvP plugin;

    public Elo(PvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDie(PlayerDeathEvent e) {//CHECK WHICH TEAM WON THE
        if (e.getEntity() instanceof Player) {
            Player player = e.getEntity();
            UUID uuid = player.getUniqueId();
            System.out.println("0");
            for (Map.Entry<String, List<String>> map : this.plugin.getUsedMaps().entrySet()) {
                System.out.println("-0");
                if (map.getValue().contains(this.plugin.getTeamInfo().get(uuid).getTeamName())) {
                    for (Map.Entry<Boolean, List<UUID>> map1 : this.plugin.getCurrentFights().entrySet()) {
                        System.out.println("-2");
                        if (map1.getKey()) {
                            if (map1.getValue().contains(uuid)) {
                                map1.getValue().remove(uuid);
                                System.out.println("-1");
                                if (map1.getValue().size() == 0) {
                                    System.out.println("1");
                                    if (this.plugin.getTeamInfo().get(uuid).getTeamName() == map.getValue().get(0)) {
                                        System.out.println("SIDE1");
                                        if (this.plugin.getTeamElo().get(map.getValue().get(0)) == null)this.plugin.getTeamElo().put(map.getValue().get(0), 1000.0);
                                        if (this.plugin.getTeamElo().get(map.getValue().get(1)) == null)this.plugin.getTeamElo().put(map.getValue().get(0), 1000.0);

                                        double P1 = (1.0 / (1.0 + Math.pow(10, ((this.plugin.getTeamElo().get(map.getValue().get(0)) - this.plugin.getTeamElo().get(map.getValue().get(1))) / 400))));
                                        double P2 = (1.0 / (1.0 + Math.pow(10, ((this.plugin.getTeamElo().get(map.getValue().get(1)) - this.plugin.getTeamElo().get(map.getValue().get(0))) / 400))));
                                        int K = 30;
                                        System.out.println(P1);
                                        System.out.println(P2);

                                        double rating1 = this.plugin.getTeamElo().get(map.getValue().get(0)) + K*(0 - P1);
                                        double rating2 = this.plugin.getTeamElo().get(map.getValue().get(1)) + K*(1 - P2);
                                        System.out.println(rating1);
                                        System.out.println(rating2);

                                        this.plugin.getTeamElo().put(map.getValue().get(0), rating1);
                                        this.plugin.getTeamElo().put(map.getValue().get(1), rating2);

                                        /*if (this.plugin.getTeamElo().get(map.getValue().get(0)) == null) {
                                            this.plugin.getTeamElo().put(map.getValue().get(0), 1195);
                                        } else {
                                            this.plugin.getTeamElo().put(map.getValue().get(0), this.plugin.getTeamElo().get(map.getValue().get(0)) - 5);
                                        }
                                        if (this.plugin.getTeamElo().get(map.getValue().get(1)) == null) {
                                            this.plugin.getTeamElo().put(map.getValue().get(1), 1205);
                                        } else {
                                            this.plugin.getTeamElo().put(map.getValue().get(1), this.plugin.getTeamElo().get(map.getValue().get(1)) + 5);
                                        }*/
                                        this.plugin.getUsedMaps().remove(map.getKey());
                                    } else {
                                        System.out.println("SIDE2");
                                        if (this.plugin.getTeamElo().get(map.getValue().get(0)) == null)this.plugin.getTeamElo().put(map.getValue().get(0), 1000.0);
                                        if (this.plugin.getTeamElo().get(map.getValue().get(1)) == null)this.plugin.getTeamElo().put(map.getValue().get(0), 1000.0);

                                        double P1 = (1.0 / (1.0 + Math.pow(10, ((this.plugin.getTeamElo().get(map.getValue().get(0)) - this.plugin.getTeamElo().get(map.getValue().get(1))) / 400))));
                                        double P2 = (1.0 / (1.0 + Math.pow(10, ((this.plugin.getTeamElo().get(map.getValue().get(1)) - this.plugin.getTeamElo().get(map.getValue().get(0))) / 400))));
                                        int K = 30;
                                        System.out.println(P1);
                                        System.out.println(P2);

                                        double rating1 = this.plugin.getTeamElo().get(map.getValue().get(1)) + K*(0 - P1);
                                        double rating2 = this.plugin.getTeamElo().get(map.getValue().get(0)) + K*(1 - P2);
                                        System.out.println(rating1);
                                        System.out.println(rating2);

                                        this.plugin.getTeamElo().put(map.getValue().get(0), rating1);
                                        this.plugin.getTeamElo().put(map.getValue().get(1), rating2);

                                        /*if (this.plugin.getTeamElo().get(map.getValue().get(1)) == null) {
                                            this.plugin.getTeamElo().put(map.getValue().get(1), 1195);
                                        } else {
                                            this.plugin.getTeamElo().put(map.getValue().get(1), this.plugin.getTeamElo().get(map.getValue().get(1)) - 5);
                                        }
                                        if (this.plugin.getTeamElo().get(map.getValue().get(0)) == null) {
                                            this.plugin.getTeamElo().put(map.getValue().get(0), 1205);
                                        } else {
                                            this.plugin.getTeamElo().put(map.getValue().get(0), this.plugin.getTeamElo().get(map.getValue().get(0)) + 5);
                                        }*/
                                        this.plugin.getUsedMaps().remove(map.getKey());
                                    }
                                }
                            }
                        } else {
                            if (map1.getValue().contains(uuid)) {
                                map1.getValue().remove(uuid);
                                if (map1.getValue().size() == 0) {
                                    if (this.plugin.getTeamInfo().get(uuid).getTeamName() == map.getValue().get(0)) {
                                        this.plugin.getUsedMaps().remove(map.getKey());
                                    } else {
                                        this.plugin.getUsedMaps().remove(map.getKey());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
