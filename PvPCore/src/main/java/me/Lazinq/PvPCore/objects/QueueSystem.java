package me.Lazinq.PvPCore.objects;

import me.Lazinq.PvPCore.PvP;
import me.Lazinq.PvPCore.data.Fight;
import me.Lazinq.PvPCore.data.Team;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.stream.Collectors;

public class QueueSystem implements Listener {

    private PvP plugin;
    public QueueSystem(PvP plugin) {
        this.plugin = plugin;
    }

    Inventory gamemodeGUIRanked = Bukkit.createInventory(null, 27, applyCC("&l&bSelect the Gamemode to play!"));
    Inventory gamemodeGUIUnranked = Bukkit.createInventory(null, 27, applyCC("&l&bSelect the Gamemode to play!"));
    //Inventory selectGUI = Bukkit.createInventory(null, 27, applyCC("&l&bSelect Players to battle!"));

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        ItemStack unranked = new ItemStack(Material.IRON_SWORD);
        ItemMeta unrankedmeta = unranked.getItemMeta();
        unrankedmeta.setDisplayName(applyCC("&l&aJoin &l&7Unranked &l&aQueue"));
        unranked.setItemMeta(unrankedmeta);
        player.getInventory().setItem(0, unranked);

        ItemStack ranked = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta rankedmeta = ranked.getItemMeta();
        rankedmeta.setDisplayName(applyCC("&l&aJoin &l&bRanked &l&aQueue"));
        ranked.setItemMeta(rankedmeta);
        player.getInventory().setItem(1, ranked);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        ItemStack unranked = new ItemStack(Material.IRON_SWORD);
        ItemMeta unrankedmeta = unranked.getItemMeta();
        unrankedmeta.setDisplayName(applyCC("&l&aJoin &l&7Unranked &l&aQueue"));
        unranked.setItemMeta(unrankedmeta);
        player.getInventory().setItem(0, unranked);

        ItemStack ranked = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta rankedmeta = ranked.getItemMeta();
        rankedmeta.setDisplayName(applyCC("&l&aJoin &l&bRanked &l&aQueue"));
        ranked.setItemMeta(rankedmeta);
        player.getInventory().setItem(1, ranked);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(player.getItemInHand().hasItemMeta()))return;

        if (applyCC(player.getItemInHand().getItemMeta().getDisplayName()).equals(applyCC("&l&aJoin &l&7Unranked &l&aQueue"))) {
            if (this.plugin.getTeamInfo().get(player.getUniqueId()) == null) {
                player.sendMessage(applyCC("&cYou are not in a team yet&f, create a team in order to start a game!"));
            }
            if (this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamRank().equalsIgnoreCase("Captain") || this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamRank().equalsIgnoreCase("Co-Leader") || this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamRank().equalsIgnoreCase("Leader")) {
                openGamemodeGUIUnranked(player);
                this.plugin.getTeamWarriors().put(player.getUniqueId(), new Fight(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName(), null, false, 0, null, null));
                System.out.println(this.plugin.getTeamWarriors().get(player.getUniqueId()).isRanked());
                System.out.println(this.plugin.getTeamWarriors().get(player.getUniqueId()).getTeam());
            } else {
                player.sendMessage(applyCC("&cMembers can't start a duel&f, ask a Captain or higher to start a duel!"));
            }
        }

        if (applyCC(player.getItemInHand().getItemMeta().getDisplayName()).equals(applyCC("&l&aJoin &l&bRanked &l&aQueue"))) {
            if (this.plugin.getTeamInfo().get(player.getUniqueId()) == null) {
                player.sendMessage(applyCC("&cYou are not in a team yet&f, create a team in order to start a game!"));
            }
            if (this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamRank().equalsIgnoreCase("Captain") || this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamRank().equalsIgnoreCase("Co-Leader") || this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamRank().equalsIgnoreCase("Leader")) {
                openGamemodeGUIRanked(player);
                this.plugin.getTeamWarriors().put(player.getUniqueId(), new Fight(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName(), null, true, 0, null, null));
                System.out.println(this.plugin.getTeamWarriors().get(player.getUniqueId()).isRanked());
                System.out.println(this.plugin.getTeamWarriors().get(player.getUniqueId()).getTeam());
            } else {
                player.sendMessage(applyCC("&cMembers can't start a duel&f, ask a Captain or higher to start a duel!"));
            }
        }
    }

    /*public void runnable() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
        }, 0L, 100L);//CHECK OM DE 10SEC OF ER TEAMS QUEUEN
    }*/


    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getTitle().contains(applyCC("&l&bSelect Players to battle!"))) {
            Player player = (Player) e.getPlayer();
            Fight fight = this.plugin.getTeamWarriors().get(e.getPlayer().getUniqueId());
            fight.setQueuedPlayers(this.plugin.getCurrentPlayers().get(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()));
            this.plugin.getCurrentPlayers().remove(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName());
            for (ItemStack contents : e.getInventory().getContents()) {
                if (contents == null) continue;
                if (contents.getItemMeta() == null) continue;
                if (contents.getItemMeta().getLore().contains(applyCC("&fSelected: &atrue"))) {
                    /*if (fight.getCurrentSize() != fight.getMaxSize()) {
                        player.sendMessage("The size is not correct! Try again.");
                        return;
                    }*/
                }
            }

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                player.sendMessage("Your team is now queing for a " + fight.getCurrentSize() + "v" + fight.getCurrentSize() + "!");
                for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
                    System.out.println(this.plugin.getTeamWarriors().get(map.getKey()).getTeam() + this.plugin.getTeamWarriors().get(map.getKey()).isRanked());
                    System.out.println(this.plugin.getTeamWarriors().get(player.getUniqueId()).getTeam() +this.plugin.getTeamWarriors().get(player.getUniqueId()).isRanked());
                        if (map.getValue().getCurrentSize() == fight.getCurrentSize()) {
                            if (this.plugin.getTeamInfo().get(map.getKey()).getTeamName() != this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()) {
                                if (map.getKey() == null) return;
                                for (Map.Entry<String, List<Location>> spawnMap : this.plugin.getSpawnLocations().entrySet()) {
                                    for (Map.Entry<String, List<String>> usedMap : this.plugin.getUsedMaps().entrySet()) {
                                        if (usedMap.getValue().contains(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()))
                                            return;
                                    }
                                    ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                                    helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                    helmet.addEnchantment(Enchantment.DURABILITY, 3);
                                    ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                    chestplate.addEnchantment(Enchantment.DURABILITY, 3);
                                    ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                                    leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                    leggings.addEnchantment(Enchantment.DURABILITY, 3);
                                    ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                    boots.addEnchantment(Enchantment.DURABILITY, 3);
                                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                                    sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                                    sword.addEnchantment(Enchantment.DURABILITY, 3);
                                    ItemStack potion = new ItemStack(Material.POTION, 1, (short) 16421);
                                    ItemStack fireres = new ItemStack(Material.POTION, 1, (short) 8259);
                                    ItemStack speed = new ItemStack(Material.POTION, 1, (short) 8226);
                                    if (this.plugin.getTeamWarriors().get(map.getKey()).isRanked() == this.plugin.getTeamWarriors().get(player.getUniqueId()).isRanked()) {

                                    List<String> strings = new ArrayList<>();
                                    strings.add(this.plugin.getTeamInfo().get(map.getKey()).getTeamName());
                                    strings.add(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName());
                                    this.plugin.getUsedMaps().put(spawnMap.getKey(), strings);
                                    this.plugin.getUsedMaps().put(spawnMap.getKey(), strings);

                                    for (UUID uuid1 : this.plugin.getTeamWarriors().get(player.getUniqueId()).getQueuedPlayers()) {
                                        Bukkit.getPlayer(uuid1).teleport(spawnMap.getValue().get(1));
                                        Bukkit.getPlayer(uuid1).sendMessage(applyCC("&6&lMatch Found!"));

                                        Bukkit.getPlayer(uuid1).getInventory().setHelmet(helmet);
                                        Bukkit.getPlayer(uuid1).getInventory().setChestplate(chestplate);
                                        Bukkit.getPlayer(uuid1).getInventory().setLeggings(leggings);
                                        Bukkit.getPlayer(uuid1).getInventory().setBoots(boots);

                                        Bukkit.getPlayer(uuid1).getInventory().setItem(0, sword);
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
                                        for (int i = 2; i <= 5; i++) {
                                            Bukkit.getPlayer(uuid1).getInventory().setItem(i, potion);
                                        }
                                        for (int i = 9; i <= 16; i++) {
                                            Bukkit.getPlayer(uuid1).getInventory().setItem(i, potion);
                                        }
                                        for (int i = 18; i <= 25; i++) {
                                            Bukkit.getPlayer(uuid1).getInventory().setItem(i, potion);
                                        }
                                        for (int i = 27; i <= 34; i++) {
                                            Bukkit.getPlayer(uuid1).getInventory().setItem(i, potion);
                                        }
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(6, fireres);
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(7, speed);
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(17, speed);
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(26, speed);
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(35, speed);
                                        Bukkit.getPlayer(uuid1).getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 64));
                                    }
                                    for (UUID uuid : map.getValue().getQueuedPlayers()) {
                                        Bukkit.getPlayer(uuid).teleport(spawnMap.getValue().get(0));
                                        Bukkit.getPlayer(uuid).sendMessage(applyCC("&6&lMatch Found!"));

                                        Bukkit.getPlayer(uuid).getInventory().setHelmet(helmet);
                                        Bukkit.getPlayer(uuid).getInventory().setChestplate(chestplate);
                                        Bukkit.getPlayer(uuid).getInventory().setLeggings(leggings);
                                        Bukkit.getPlayer(uuid).getInventory().setBoots(boots);

                                        Bukkit.getPlayer(uuid).getInventory().setItem(0, sword);
                                        Bukkit.getPlayer(uuid).getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
                                        for (int i = 2; i <= 5; i++) {
                                            Bukkit.getPlayer(uuid).getInventory().setItem(i, potion);
                                        }
                                        for (int i = 9; i <= 16; i++) {
                                            Bukkit.getPlayer(uuid).getInventory().setItem(i, potion);
                                        }
                                        for (int i = 18; i <= 25; i++) {
                                            Bukkit.getPlayer(uuid).getInventory().setItem(i, potion);
                                        }
                                        for (int i = 27; i <= 34; i++) {
                                            Bukkit.getPlayer(uuid).getInventory().setItem(i, potion);
                                        }
                                        Bukkit.getPlayer(uuid).getInventory().setItem(6, fireres);
                                        Bukkit.getPlayer(uuid).getInventory().setItem(7, speed);
                                        Bukkit.getPlayer(uuid).getInventory().setItem(17, speed);
                                        Bukkit.getPlayer(uuid).getInventory().setItem(26, speed);
                                        Bukkit.getPlayer(uuid).getInventory().setItem(35, speed);
                                        Bukkit.getPlayer(uuid).getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 64));
                                    }
                                    this.plugin.getCurrentFights().put(this.plugin.getTeamWarriors().get(map.getKey()).isRanked(), this.plugin.getTeamWarriors().get(map.getKey()).getQueuedPlayers());
                                    this.plugin.getCurrentFights().put(this.plugin.getTeamWarriors().get(player.getUniqueId()).isRanked(), this.plugin.getTeamWarriors().get(player.getUniqueId()).getQueuedPlayers());
                                    this.plugin.getTeamWarriors().remove(player.getUniqueId());
                                    this.plugin.getTeamWarriors().remove(map.getKey());
                                }
                            }
                        }
                    }
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null)return;

        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().contains(applyCC("&l&bSelect Players to battle!"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {
                SkullMeta im = (SkullMeta) e.getCurrentItem().getItemMeta();
                UUID skullPlayer = Bukkit.getPlayer(im.getOwner()).getUniqueId();
                final Fight fight = this.plugin.getTeamWarriors().get(player.getUniqueId());
                List<UUID> players = new ArrayList<>();

                if (fight.getCurrentSize() <= fight.getMaxSize()) {
                    if (e.getClick() == ClickType.LEFT) {
                        List<String> lore = new ArrayList<>();
                        if (im.hasLore()) {
                            lore = im.getLore();
                        }
                        if (lore.contains(applyCC("&fSelected: &cfalse"))) {
                            lore.remove(applyCC("&fSelected: &cfalse"));
                            lore.add(applyCC("&fSelected: &atrue"));
                            im.setLore(lore);
                            e.getCurrentItem().setItemMeta(im);
                            player.updateInventory();
                        }
                        if (this.plugin.getCurrentPlayers().get(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()) == null || !this.plugin.getCurrentPlayers().get(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()).contains(skullPlayer)) {
                            players.add(skullPlayer);
                            this.plugin.getCurrentPlayers().put(this.plugin.getTeamInfo().get(skullPlayer).getTeamName(), players);
                            System.out.println(fight.getCurrentSize());
                            fight.setCurrentSize(fight.getCurrentSize() + 1);
                            System.out.println(fight.getCurrentSize());
                        }
                    }
                }
                if (e.getClick() == ClickType.RIGHT) {
                    List<String> lore = new ArrayList<>();
                    if (im.hasLore()) {
                        lore = im.getLore();
                    }
                    if (lore.contains(applyCC("&fSelected: &atrue"))) {
                        lore.remove(applyCC("&fSelected: &atrue"));
                        lore.add(applyCC("&fSelected: &cfalse"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                        player.updateInventory();
                        System.out.println("NO");
                        if (this.plugin.getCurrentPlayers().get(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()) != null) {
                        if (this.plugin.getCurrentPlayers().get(this.plugin.getTeamInfo().get(player.getUniqueId()).getTeamName()).contains(skullPlayer)) {
                            System.out.println("YES");
                            players.remove(skullPlayer);
                            this.plugin.getCurrentPlayers().put(this.plugin.getTeamInfo().get(skullPlayer).getTeamName(), players);
                            System.out.println(fight.getCurrentSize());
                            fight.setCurrentSize(fight.getCurrentSize() - 1);
                            System.out.println(fight.getCurrentSize());
                        }
                    }
                    }
                }
            }
        }
        if (e.getInventory().equals(gamemodeGUIRanked)) {
            e.setCancelled(true);
            final Fight fight = this.plugin.getTeamWarriors().get(player.getUniqueId());
            if (e.getSlot() == 0) {
                fight.setMaxSize(2);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 2) {
                fight.setMaxSize(4);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 4) {
                fight.setMaxSize(6);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 6) {
                fight.setMaxSize(8);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 8) {
                fight.setMaxSize(10);openSelectGUI(player);fight.setCurrentSize(0);
            }
        }
        if (e.getInventory().equals(gamemodeGUIUnranked)) {
            e.setCancelled(true);
            final Fight fight = this.plugin.getTeamWarriors().get(player.getUniqueId());
            if (e.getSlot() == 0) {
                fight.setMaxSize(2);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 2) {
                fight.setMaxSize(4);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 4) {
                fight.setMaxSize(6);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 6) {
                fight.setMaxSize(8);openSelectGUI(player);fight.setCurrentSize(0);
            }
            if (e.getSlot() == 8) {
                fight.setMaxSize(10);openSelectGUI(player);fight.setCurrentSize(0);
            }
        }
    }

    public void openGamemodeGUIRanked(Player player) {//LORE
            ItemStack versus2 = new ItemStack(Material.GLASS);
            ItemMeta versus2meta = versus2.getItemMeta();
            List<String> lore = new ArrayList<>();
            List integers = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 2) {
                if (map.getValue().isRanked()) {
                    integers.add(1);
                }
            }
            }
            lore.add(applyCC("&cQueing: " + integers.size()));
            versus2meta.setLore(lore);
            versus2meta.setDisplayName(applyCC("&aClick to play 2v2!"));
            versus2.setItemMeta(versus2meta);
            ItemStack versus4 = new ItemStack(Material.GLASS);
            ItemMeta versus4meta = versus4.getItemMeta();
        List<String> lore4 = new ArrayList<>();
        List integers4 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 4) {
                if (map.getValue().isRanked()) {
                    integers4.add(1);
                }
            }
        }
        lore4.add(applyCC("&cQueing: " + integers4.size()));
        versus4meta.setLore(lore4);
            versus4meta.setDisplayName(applyCC("&aClick to play 4v4!"));
            versus4.setItemMeta(versus4meta);
            ItemStack versus6 = new ItemStack(Material.GLASS);
            ItemMeta versus6meta = versus6.getItemMeta();
        List<String> lore6 = new ArrayList<>();
        List integers6 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 6) {
                if (map.getValue().isRanked()) {
                    integers6.add(1);
                }
            }
        }
        lore6.add(applyCC("&cQueing: " + integers6.size()));
        versus6meta.setLore(lore6);
            versus6meta.setDisplayName(applyCC("&aClick to play 6v6!"));
            versus6.setItemMeta(versus6meta);
            ItemStack versus8 = new ItemStack(Material.GLASS);
            ItemMeta versus8meta = versus8.getItemMeta();
        List<String> lore8 = new ArrayList<>();
        List integers8 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 8) {
                if (map.getValue().isRanked()) {
                    integers8.add(1);
                }
            }
        }
        lore8.add(applyCC("&cQueing: " + integers8.size()));
        versus8meta.setLore(lore8);
            versus8meta.setDisplayName(applyCC("&aClick to play 8v8!"));
            versus8.setItemMeta(versus8meta);
            ItemStack versus10 = new ItemStack(Material.GLASS);
            ItemMeta versus10meta = versus10.getItemMeta();
        List<String> lore10 = new ArrayList<>();
        List integers10 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
                if (map.getValue().getCurrentSize() == 10) {
                    if (map.getValue().isRanked()) {
                    integers10.add(1);
                }
            }
        }
        lore10.add(applyCC("&cQueing: " + integers10.size()));
        versus10meta.setLore(lore10);
            versus10meta.setDisplayName(applyCC("&aClick to play 10v10!"));
            versus10.setItemMeta(versus10meta);

        gamemodeGUIRanked.setItem(0, versus2);
        gamemodeGUIRanked.setItem(2, versus4);
        gamemodeGUIRanked.setItem(4, versus6);
        gamemodeGUIRanked.setItem(6, versus8);
        gamemodeGUIRanked.setItem(8, versus10);
        player.openInventory(gamemodeGUIRanked);
    }

    public void openGamemodeGUIUnranked(Player player) {//LORE
        ItemStack versus2 = new ItemStack(Material.GLASS);
        ItemMeta versus2meta = versus2.getItemMeta();
        List<String> lore = new ArrayList<>();
        List integers = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 2) {
                if (!map.getValue().isRanked()) {
                    integers.add(1);
                }
            }
        }
        lore.add(applyCC("&cQueing: " + integers.size()));
        versus2meta.setLore(lore);
        versus2meta.setDisplayName(applyCC("&aClick to play 2v2!"));
        versus2.setItemMeta(versus2meta);
        ItemStack versus4 = new ItemStack(Material.GLASS);
        ItemMeta versus4meta = versus4.getItemMeta();
        List<String> lore4 = new ArrayList<>();
        List integers4 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 4) {
                if (!map.getValue().isRanked()) {
                    integers4.add(1);
                }
            }
        }
        lore4.add(applyCC("&cQueing: " + integers4.size()));
        versus4meta.setLore(lore4);
        versus4meta.setDisplayName(applyCC("&aClick to play 4v4!"));
        versus4.setItemMeta(versus4meta);
        ItemStack versus6 = new ItemStack(Material.GLASS);
        ItemMeta versus6meta = versus6.getItemMeta();
        List<String> lore6 = new ArrayList<>();
        List integers6 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 6) {
                if (!map.getValue().isRanked()) {
                    integers6.add(1);
                }
            }
        }
        lore6.add(applyCC("&cQueing: " + integers6.size()));
        versus6meta.setLore(lore6);
        versus6meta.setDisplayName(applyCC("&aClick to play 6v6!"));
        versus6.setItemMeta(versus6meta);
        ItemStack versus8 = new ItemStack(Material.GLASS);
        ItemMeta versus8meta = versus8.getItemMeta();
        List<String> lore8 = new ArrayList<>();
        List integers8 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 8) {
                if (!map.getValue().isRanked()) {
                    integers8.add(1);
                }
            }
        }
        lore8.add(applyCC("&cQueing: " + integers8.size()));
        versus8meta.setLore(lore8);
        versus8meta.setDisplayName(applyCC("&aClick to play 8v8!"));
        versus8.setItemMeta(versus8meta);
        ItemStack versus10 = new ItemStack(Material.GLASS);
        ItemMeta versus10meta = versus10.getItemMeta();
        List<String> lore10 = new ArrayList<>();
        List integers10 = new ArrayList();
        for (Map.Entry<UUID, Fight> map : this.plugin.getTeamWarriors().entrySet()) {
            if (map.getValue().getCurrentSize() == 10) {
                if (!map.getValue().isRanked()) {
                    integers10.add(1);
                }
            }
        }
        lore10.add(applyCC("&cQueing: " + integers10.size()));
        versus10meta.setLore(lore10);
        versus10meta.setDisplayName(applyCC("&aClick to play 10v10!"));
        versus10.setItemMeta(versus10meta);

        gamemodeGUIUnranked.setItem(0, versus2);
        gamemodeGUIUnranked.setItem(2, versus4);
        gamemodeGUIUnranked.setItem(4, versus6);
        gamemodeGUIUnranked.setItem(6, versus8);
        gamemodeGUIUnranked.setItem(8, versus10);
        player.openInventory(gamemodeGUIUnranked);
    }

    public void openSelectGUI(Player player) {
        UUID uuid = player.getUniqueId();
        final Fight fight = this.plugin.getTeamWarriors().get(uuid);
        final Inventory inv = Bukkit.createInventory(null, 27, applyCC("&l&bSelect Players to battle!"));
         inv.clear();
        if (!this.plugin.getTeamInfo().isEmpty()) {
            for (Map.Entry<UUID, Team> map : this.plugin.getTeamInfo().entrySet()) {
                if (map.getValue().getTeamName().equals(this.plugin.getTeamInfo().get(uuid).getTeamName())) {
                    this.plugin.getTeamWarriors().put(uuid, new Fight(map.getValue().getTeamName(), null, fight.isRanked(), fight.getCurrentSize(), fight.getMaxSize(), null));
                    ItemStack playerhead = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                    SkullMeta meta = (SkullMeta) playerhead.getItemMeta();
                    meta.setOwner(Bukkit.getPlayer(map.getKey()).getName());
                    meta.setDisplayName(applyCC("&l&c" + Bukkit.getPlayer(map.getKey()).getDisplayName()));
                    List<String> lore = new ArrayList<>();
                    lore.add(applyCC("&fSelected: &cfalse"));
                    meta.setLore(lore);
                    playerhead.setItemMeta(meta);

                    inv.addItem(playerhead);
                }
            }
        }
        player.openInventory(inv);
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
