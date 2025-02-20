package org.mesmeralis.OITC.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.Objects;
import java.util.Random;

public class EntityKillListener implements Listener {

    public Main main;
    public GameManager manager;
    public SQLGetter data;

    public EntityKillListener(Main main) {
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }
    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            if (event.getEntity().getKiller() != null) {
                Player killer = event.getEntity().getKiller();
                if (manager.isGameRunning) {
                    data.addPoints(killer.getUniqueId(), 2);
                    killer.sendMessage(ColourUtils.colour(manager.prefix + "&aYou earned 2 points for killing a zombie."));
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            if (event.getEntity().getKiller() instanceof Zombie) {
                Player killed = (Player) event.getEntity();
                Random randomLoc = new Random();
                if (manager.isGameRunning) {
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + killed.getName() + "&e was slain by a &2Zombie&e."));
                    data.addDeaths(killed.getUniqueId(), 1);
                    manager.playerLives.put(killed, manager.playerLives.get(killed) - 1);
                    if (this.main.map.get(killed.getUniqueId()).points > 3) {
                        data.addPoints(killed.getUniqueId(), -3);
                    }
                    if (manager.playerLives.get(killed) > 1) {
                        int locNumber = randomLoc.nextInt(10) + 1;
                        Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                                main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                        killed.setRespawnLocation(location);
                        killed.playSound(Objects.requireNonNull(killed.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                        killed.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), ColourUtils.colour("&7You have &c" + manager.playerLives.get(killed) + "&7 lives left."), 0, 20, 10);
                    }
                    else {
                        int locNumber = randomLoc.nextInt(10) + 1;
                        Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                                main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                        killed.setRespawnLocation(location);
                        Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + killed.getName() + "&e was eliminated by a &2Zombie&e!"));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                            killed.setGameMode(GameMode.SPECTATOR);
                        }, 10L);
                        killed.playSound(Objects.requireNonNull(killed.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                        killed.sendTitle(ColourUtils.colour("&4&lELIMINATED!"), "", 0, 20, 10);
                        manager.playersInGame.remove(killed);
                        manager.playerLives.put(killed, 0);
                        if (manager.playersInGame.size() == 1) {
                            manager.startEndingTimer();
                        }
                    }
                }
            } if (event.getEntity().getKiller() != null){
                Player killed = (Player) event.getEntity();
                Player killer = event.getEntity().getKiller();
                Random randomLoc = new Random();
                if (manager.isGameRunning) {
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + killed.getName() + "&e was slain by &a" + killer.getName() + "&e."));
                    data.addDeaths(killed.getUniqueId(), 1);
                    data.addKills(killer.getUniqueId(), 1);
                    data.addPoints(killer.getUniqueId(), 5);
                    manager.playerLives.put(killed, manager.playerLives.get(killed) - 1);
                    killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                    if (this.main.map.get(killed.getUniqueId()).points > 3) {
                        data.addPoints(killed.getUniqueId(), -3);
                    }
                    if (manager.playerLives.get(killed) > 1) {
                        int locNumber = randomLoc.nextInt(10) + 1;
                        Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                                main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                        killed.setRespawnLocation(location);
                        killed.playSound(Objects.requireNonNull(killed.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                        killed.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), ColourUtils.colour("&7You have &c" + manager.playerLives.get(killed) + "&7 lives left."), 0, 20, 10);
                    } else {
                        int locNumber = randomLoc.nextInt(10) + 1;
                        Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                                main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                                main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                        killed.setRespawnLocation(location);
                        Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + killed.getName() + "&e was eliminated by a &2Zombie&e!"));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                            killed.setGameMode(GameMode.SPECTATOR);
                        }, 10L);
                        killed.playSound(Objects.requireNonNull(killed.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                        killed.sendTitle(ColourUtils.colour("&4&lELIMINATED!"), "", 0, 20, 10);
                        manager.playersInGame.remove(killed);
                        manager.playerLives.put(killed, 0);
                        if (manager.playersInGame.size() == 1) {
                            manager.startEndingTimer();
                        }
                    }
                }
            }
        }

    }

}
