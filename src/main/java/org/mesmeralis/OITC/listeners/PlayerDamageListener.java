package org.mesmeralis.OITC.listeners;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.Objects;
import java.util.Random;

public class PlayerDamageListener implements Listener {


    public GameManager manager;
    public SQLGetter data;
    public Main main;
    Random randomLoc = new Random();

    public PlayerDamageListener(Main main) {
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (manager.isGameRunning) {
                Player damager = (Player) event.getDamager();
                if (!(damager.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD)) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getDamager() instanceof Arrow) {
            if (event.getEntity() instanceof Player) {
                Player damaged = (Player) event.getEntity();
                Arrow arrow = (Arrow) event.getDamager();
                Player shooter = (Player) arrow.getShooter();
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + damaged.getName() + "&e was shot by &a" + shooter.getName() + "&e."));
                data.addKills(shooter.getUniqueId(), 1);
                this.manager.gameKills.put(shooter.getPlayer(), this.manager.gameKills.get(shooter) + 1);
                data.addDeaths(damaged.getUniqueId(), 1);
                data.addPoints(shooter.getUniqueId(), 5);
                manager.playerLives.put(damaged, manager.playerLives.get(damaged) - 1);
                shooter.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                if (this.main.map.get(damaged.getUniqueId()).points > 3) {
                    data.addPoints(damaged.getUniqueId(), -3);
                }
                if (manager.playerLives.get(damaged) > 1) {
                    int locNumber = randomLoc.nextInt(10) + 1;
                    Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                            main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                            main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                            main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                    damaged.teleport(location);
                    damaged.setHealth(20);
                    arrow.remove();
                    damaged.playSound(Objects.requireNonNull(damaged.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                    damaged.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), ColourUtils.colour("&7You have &c" + manager.playerLives.get(damaged) + "&7 lives left."), 0, 20, 10);
                    damaged.setArrowsInBody(0);
                }
                else {
                    int locNumber = randomLoc.nextInt(10) + 1;
                    Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                            main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                            main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                            main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                    damaged.teleport(location);
                    damaged.setHealth(20);
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + damaged.getName() + "&e was eliminated by &a" + shooter.getName() + "&e!"));
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                        damaged.setGameMode(GameMode.SPECTATOR);
                    }, 10L);
                    arrow.remove();
                    damaged.playSound(Objects.requireNonNull(damaged.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                    damaged.sendTitle(ColourUtils.colour("&4&lELIMINATED!"), "", 0, 20, 10);
                    damaged.setArrowsInBody(0);
                    manager.playerLives.put(damaged, 0);
                    manager.playersInGame.remove(damaged);
                    if (manager.playersInGame.size() == 1) {
                        manager.startEndingTimer();
                    }
                }
            }
        }


    }
}
