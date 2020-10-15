package me.Lazinq.PvPCore.commands;

import me.Lazinq.PvPCore.PvP;
import me.Lazinq.PvPCore.data.Fight;
import me.Lazinq.PvPCore.data.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Teams implements CommandExecutor {

    private PvP plugin;

    public Teams(PvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (label.equalsIgnoreCase("team")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                        if (map.getValue().getTeamName().equalsIgnoreCase(args[1])) {
                            player.sendMessage(applyCC("&fA team with this name already consists!"));
                            return true;
                        }
                        if (map.getKey().equals(uuid)) {
                            player.sendMessage(applyCC("&cYou are in a team already&f, try leaving/disbanding first!"));
                            return true;
                        }
                    }
                    Bukkit.broadcastMessage(applyCC("&c"+player.getDisplayName() + " &fcreated the team called &c&l" + args[1]));
                    this.plugin.getTeamInfo().put(uuid, new Team(args[1], "Leader", 0, 20));
                }
                if (args[0].equalsIgnoreCase("rename")) {//CHECK IF NAMEISNT TAKEN YET
                    if (this.plugin.getTeamInfo().get(uuid) != null) {
                        if (this.plugin.getTeamInfo().get(uuid).getTeamRank() == "Leader" || this.plugin.getTeamInfo().get(uuid).getTeamRank() == "Co-Leader") {
                            Bukkit.broadcastMessage(applyCC("&c" +player.getDisplayName() + " &frenamed their team from &c" + this.plugin.getTeamInfo().get(uuid).getTeamName() + " &fto&c " + args[1]));
                            for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                                if (map.getValue().getTeamName().equalsIgnoreCase(this.plugin.getTeamInfo().get(uuid).getTeamName())) {
                                    if (map.getValue().getTeamRank().equals("Leader")) {
                                        this.plugin.getTeamInfo().put(map.getKey(), new Team(args[1], "Leader", 0, 19));
                                    }
                                    if (map.getValue().getTeamRank().equals("Co-Leader")) {
                                        this.plugin.getTeamInfo().put(map.getKey(), new Team(args[1], "Co-Leader", 0, 19));
                                    }
                                    if (map.getValue().getTeamRank().equals("Captain")) {
                                        this.plugin.getTeamInfo().put(map.getKey(), new Team(args[1], "Captain", 0, 19));
                                    }
                                    if (map.getValue().getTeamRank().equals("Member")) {
                                        this.plugin.getTeamInfo().put(map.getKey(), new Team(args[1], "Member", 0, 19));
                                    }
                                }
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                            if (this.plugin.getTeamInfo().get(map.getKey()).getTeamRank().equalsIgnoreCase("Leader") || this.plugin.getTeamInfo().get(map.getKey()).getTeamRank().equalsIgnoreCase("Co-Leader")) {
                                Player target = Bukkit.getServer().getPlayer(args[1]);
                                if (target == player)return true;
                                if (this.plugin.getTeamInfo().get(target.getUniqueId()) != null) {
                                    if (this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName().equals(this.plugin.getTeamInfo().get(uuid).getTeamName()))
                                        return true;
                                }
                                if (target == null || args.length == 0) {
                                    player.sendMessage(applyCC("&cIncorrect input&f, try /Team Invite <Player>."));
                                    return true;
                                }
                                target.sendMessage(applyCC("&c"+player.getDisplayName() + " &finvited you to their team called&c " + this.plugin.getTeamInfo().get(uuid).getTeamName()));
                                player.sendMessage(applyCC("&fYou invited &c" + target.getDisplayName() + " &fto your team!"));
                                this.plugin.getPendingInvites().put(target.getUniqueId(), uuid);
                            }
                        }
                }
                if (args[0].equalsIgnoreCase("Join")) {
                    if (this.plugin.getPendingInvites().containsKey(uuid)) {

                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage(applyCC("&cIncorrect input&f, try /Team Join <Player>."));
                            return true;
                        }
                        if (!this.plugin.getPendingInvites().containsValue(target.getUniqueId())) {
                            player.sendMessage(applyCC("&c" + target.getDisplayName() + " &fhas not invited you!"));
                            return true;
                        }
                        if (this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName() != null) {

                            if (this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize() < this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()) {

                                for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                                    if (map.getValue().getTeamName().equals(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName())) {

                                        if (map.getValue().getTeamRank().equals("Leader")) {
                                            this.plugin.getTeamInfo().put(map.getKey(), new Team(this.plugin.getTeamInfo().get(map.getKey()).getTeamName(), "Leader", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize() + 1, 20));
                                        }
                                        if (map.getValue().getTeamRank().equals("Co-Leader")) {
                                            this.plugin.getTeamInfo().put(map.getKey(), new Team(this.plugin.getTeamInfo().get(map.getKey()).getTeamName(), "Co-Leader", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize() + 1, 20));
                                        }
                                        if (map.getValue().getTeamRank().equals("Captain")) {
                                            this.plugin.getTeamInfo().put(map.getKey(), new Team(this.plugin.getTeamInfo().get(map.getKey()).getTeamName(), "Captain", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize() + 1, 20));
                                        }
                                        if (map.getValue().getTeamRank().equals("Member")) {
                                            this.plugin.getTeamInfo().put(map.getKey(), new Team(this.plugin.getTeamInfo().get(map.getKey()).getTeamName(), "Member", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize() + 1, 20));
                                        }
                                    }
                                }
                                player.sendMessage(applyCC("&cYou&f succesfully joined &c" + this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName()));
                                this.plugin.getTeamInfo().put(uuid, new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Member", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), 20));
                            System.out.println(Bukkit.getPlayer(uuid).getName());
                            System.out.println(this.plugin.getTeamInfo().get(uuid).getTeamName());
                                System.out.println(this.plugin.getTeamInfo().get(uuid).getTeamRank());
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("Kick")) {
                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(applyCC("&cIncorrect input&f, try /Team Kick <Player>."));
                        return true;
                    }
                    if (this.plugin.getTeamInfo().containsKey(uuid)) {
                        if (this.plugin.getTeamInfo().get(uuid).getTeamRank().equalsIgnoreCase("Leader") || this.plugin.getTeamInfo().get(uuid).getTeamRank().equalsIgnoreCase("Co-Leader")) {
                            if (this.plugin.getTeamInfo().containsKey(target.getUniqueId())) {
                                this.plugin.getTeamInfo().remove(target.getUniqueId());
                                StringBuilder message = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    message.append(" ").append(args);
                                }
                                target.sendMessage(applyCC("&cYou &fwere kicked from&c " + this.plugin.getTeamInfo().get(uuid).getTeamName() + " &fbecause of &c" + message.toString()));
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("Leave")) {
                    if (this.plugin.getTeamInfo().containsKey(uuid)) {
                        if (this.plugin.getTeamInfo().get(uuid).getTeamRank() == "Leader") {
                            player.sendMessage("&cYou can't leave this team because you are the leader, &fTry disbanding it or leader someone else instead.");
                        } else {
                            this.plugin.getTeamInfo().remove(uuid);
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("Help")) {
                    if (args.length == 1) {
                        player.sendMessage(applyCC("&7&l---- &c&l1/2 &7&l----"));
                        player.sendMessage(applyCC("&c/Team Create <Name>: &fCreate a team"));
                        player.sendMessage(applyCC("&c/Team Invite <Player>: &fInvite a player"));
                        player.sendMessage(applyCC("&c/Team Join <Team>/<Player>: &fJoin a Team or Inviter's team"));
                        player.sendMessage(applyCC("&c/Team Kick <Player>: &fKick a player from your team"));
                        player.sendMessage(applyCC("&c/Team Info <Player>/<Team>: &fShows information about a Team"));
                        player.sendMessage(applyCC("&c/Team Help: &fDisplays a list of commands"));
                        player.sendMessage(applyCC("&7&l---- &c&l1/2 &7&l----"));
                    } else {
                        if (args[1].equals("1")) {
                            player.sendMessage(applyCC("&7&l---- &c&l1/2 &7&l----"));
                            player.sendMessage(applyCC("&c/Team Create <Name>: &fCreate a team"));
                            player.sendMessage(applyCC("&c/Team Invite <Player>: &fInvite a player"));
                            player.sendMessage(applyCC("&c/Team Join <Player>: &fJoin a Inviter's team"));
                            player.sendMessage(applyCC("&c/Team Kick <Player>: &fKick a player from your team"));
                            player.sendMessage(applyCC("&c/Team Info <Player>/<Team>: &fShows information about a Team"));
                            player.sendMessage(applyCC("&c/Team Help: &fDisplays a list of commands"));
                            player.sendMessage(applyCC("&7&l---- &c&l1/2 &7&l----"));
                        }
                        if (args[1].equals("2")) {
                            player.sendMessage(applyCC("&7&l---- &c&l2/2 &7&l----"));
                            player.sendMessage(applyCC("&c/Team Rename <Name>: &fRename your team"));
                            player.sendMessage(applyCC("&c/Team Promote <Player>: &fPromote a player in your team"));
                            player.sendMessage(applyCC("&c/Team Demote <Player>: &fDemote a player in your team"));
                            player.sendMessage(applyCC("&c/Team Leader <Name>: &fLeader a player in your team"));
                            player.sendMessage(applyCC("&c/Team Disband: &fDisband your team"));
                            player.sendMessage(applyCC("&c/Team Disband Confirm: &fConfirm your disband"));
                            player.sendMessage(applyCC("&7&l---- &c&l2/2 &7&l----"));
                        }
                    }
                    //7 Lines each page
                }
                if (args[0].equalsIgnoreCase("Info")) {//BOTH FOR PLAYER AND TEAM
                    if (Bukkit.getServer().getPlayer(args[1]) == null) {
                        StringBuilder team = new StringBuilder();
                        StringBuilder leader = new StringBuilder();
                        StringBuilder coleader = new StringBuilder();
                        StringBuilder captain = new StringBuilder();
                        StringBuilder member = new StringBuilder();
                        StringBuilder size = new StringBuilder();
                        StringBuilder elo = new StringBuilder();

                        for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                            if (map.getValue().getTeamName().equalsIgnoreCase(args[1])) {
                                System.out.println("niff");
                                System.out.println(Bukkit.getPlayer(map.getKey()).getName());
                                if (!team.toString().contains(args[1])) {
                                    team.append(", ").append(args[1]);
                                }
                                if (map.getValue().getTeamRank().equals("Leader")) {
                                    if (!leader.toString().contains(Bukkit.getPlayer(map.getKey()).getName())) {
                                    leader.append(", ").append(Bukkit.getPlayer(map.getKey()).getName());
                                    }
                                }
                                if (map.getValue().getTeamRank().equals("Co-Leader")) {
                                    if (!coleader.toString().contains(Bukkit.getPlayer(map.getKey()).getName())) {
                                        coleader.append(", ").append(Bukkit.getPlayer(map.getKey()).getName());
                                    }
                                }
                                if (map.getValue().getTeamRank().equals("Captain")) {
                                    if (!captain.toString().contains(Bukkit.getPlayer(map.getKey()).getName())) {
                                        captain.append(", ").append(Bukkit.getPlayer(map.getKey()).getName());
                                    }
                                }
                                if (map.getValue().getTeamRank().equals("Member")) {
                                    if (!member.toString().contains(Bukkit.getPlayer(map.getKey()).getName())) {
                                        member.append(", ").append(Bukkit.getPlayer(map.getKey()).getName());
                                    }
                                }
                                if (!size.toString().contains(map.getValue().getTeamCurrentSize()+1 + "")) {
                                    size.append(", ").append(map.getValue().getTeamCurrentSize() + 1);
                                }
                                if (!elo.toString().contains(this.plugin.getTeamElo().get(args[1]) + "")) {
                                    elo.append(", ").append(this.plugin.getTeamElo().get(args[1]));
                                }
                            }
                        }
                        player.sendMessage(applyCC("&fInfo about team: &c&l" + team.toString().substring(2)));
                        player.sendMessage(applyCC("&fLeader: &c&l" + leader.toString().substring(2)));
                        if (coleader.toString().length() > 2) {
                            player.sendMessage(applyCC("&fCo-Leader(s): &c&l" + coleader.toString().substring(2)));
                        } else {
                            player.sendMessage(applyCC("&fCo-Leader(s): &c&l"));
                        }
                        if (captain.toString().length() > 2) {
                            player.sendMessage(applyCC("&fCaptain(s): &c&l" + captain.toString().substring(2)));
                        } else {
                            player.sendMessage(applyCC("&fCaptain(s): &c&l"));
                        }
                        if (member.toString().length() > 2) {
                            player.sendMessage(applyCC("&fMember(s): &c&l" + member.toString().substring(2)));
                        } else {
                            player.sendMessage(applyCC("&fMember(s): &c&l"));
                        }

                        player.sendMessage(applyCC("&fTeam Size: &c&l" + size.toString().substring(2) + "/20"));
                        if (this.plugin.getTeamElo().get(args[1]) == null) {
                            player.sendMessage(applyCC("&fElo: &c&l1200"));
                        } else {
                            player.sendMessage(applyCC("&fElo: &c&l" + elo.toString().substring(2)));
                        }

                    } else {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (this.plugin.getTeamInfo().containsKey(target.getUniqueId())) {
                            this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName();
                            //TeamName,:,TeamSize,:,Elo,:,Leader,:,Co-Leaders,:,Captains,:,Members
                            player.sendMessage("InfoAboutTheirTeam");
                            player.sendMessage("" + this.plugin.getTeamElo().get(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName()));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("promote")) {
                    if (this.plugin.getTeamInfo().containsKey(uuid)) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        String teamRankPlayer = this.plugin.getTeamInfo().get(uuid).getTeamRank();
                        String teamRankTarget = this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamRank();
                        if (this.plugin.getTeamInfo().containsKey(target.getUniqueId())) {
                            if (target == null) {
                                player.sendMessage("Incorrect input, try /Team Promote (Player).");
                                return true;
                            }
                            if (teamRankTarget.equals("Member")) {
                                if (teamRankPlayer.equals("Captain") || teamRankPlayer.equals("Co-Leader") || teamRankPlayer.equals("Leader")) {
                                    this.plugin.getTeamInfo().put(target.getUniqueId(), new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Captain", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()));
                                    player.sendMessage("You promoted "+target.getDisplayName()+" to Captain!");
                                    target.sendMessage(player.getDisplayName()+" promoted you to Captain!");
                                }
                            }
                            if (teamRankTarget.equals("Captain")) {
                                if (teamRankPlayer.equals("Co-Leader") || teamRankPlayer.equals("Leader")) {
                                    this.plugin.getTeamInfo().put(target.getUniqueId(), new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Co-Leader", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()));
                                    player.sendMessage("You promoted "+target.getDisplayName()+" to Co-Leader!");
                                    target.sendMessage(player.getDisplayName()+" promoted you to Co-Leader!");
                                }
                            }
                            if (teamRankTarget.equals("Co-Leader")) {
                                if (teamRankPlayer.equals("Leader")) {
                                    player.sendMessage("You can't promote someone to leader, try using /team leader (Player)");
                                }
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("Demote")) {
                    if (this.plugin.getTeamInfo().containsKey(uuid)) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        String teamRankPlayer = this.plugin.getTeamInfo().get(uuid).getTeamRank();
                        String teamRankTarget = this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamRank();
                        if (this.plugin.getTeamInfo().containsKey(target.getUniqueId())) {
                            if (target == null) {
                                player.sendMessage("Incorrect input, try /Team Demote (Player).");
                                return true;
                            }
                            if (teamRankTarget.equals("Member")) {
                                player.sendMessage("This player already has the rank Member! Try kicking them instead.");
                            }
                            if (teamRankTarget.equals("Captain")) {
                                if (teamRankPlayer.equals("Co-Leader") || teamRankPlayer.equals("Leader")) {
                                    this.plugin.getTeamInfo().put(target.getUniqueId(), new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Member", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()));
                                    player.sendMessage("You demoted "+target.getDisplayName()+" to Member!");
                                    target.sendMessage(player.getDisplayName()+" demoted you to Member!");
                                }
                            }
                            if (teamRankTarget.equals("Co-Leader")) {
                                if (teamRankPlayer.equals("Leader")) {
                                    this.plugin.getTeamInfo().put(target.getUniqueId(), new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Captain", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()));
                                    player.sendMessage("You demoted "+target.getDisplayName()+" to Captain!");
                                    target.sendMessage(player.getDisplayName()+" demoted you to Captain!");
                                }
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("Leader")) {
                    if (this.plugin.getTeamInfo().containsKey(uuid)) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (this.plugin.getTeamInfo().get(uuid).getTeamRank().equalsIgnoreCase("Leader")) {
                            this.plugin.getTeamInfo().put(target.getUniqueId(), new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Leader", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()));
                            this.plugin.getTeamInfo().put(uuid, new Team(this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamName(), "Co-Leader", this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamCurrentSize(), this.plugin.getTeamInfo().get(target.getUniqueId()).getTeamMaxSize()));
                            player.sendMessage("You made "+target.getDisplayName()+" the Leader!");
                            target.sendMessage(player.getDisplayName()+" made you the Leader!");
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("Disband")) {
                    if (args.length == 1) {
                        if (this.plugin.getTeamInfo().get(uuid).getTeamRank().equalsIgnoreCase("Leader")) {
                            this.plugin.getPendingRemoval().put(uuid, System.currentTimeMillis());
                            player.sendMessage("Confirm your disband using /Team disband confirm, you have 60 seconds to do this.");
                        }
                    }
                        else if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("Confirm")) {
                                if (this.plugin.getPendingRemoval().containsKey(uuid) && this.plugin.getPendingRemoval().get(uuid) + 60000L >= System.currentTimeMillis()) {
                                    player.sendMessage("You disbanded your team!");
                                    for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                                        if (map.getValue().getTeamName() == this.plugin.getTeamInfo().get(uuid).getTeamName()) {
                                            this.plugin.getTeamInfo().remove(map.getKey());
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
        return false;
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
