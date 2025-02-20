package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
    public PlayerDamageListener (Main main) {
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
           if(manager.isGameRunning) {
               event.setCancelled(true);
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
                    if(this.main.map.get(damaged.getUniqueId()).points > 3) {
                        data.addPoints(damaged.getUniqueId(), -3);
                    }
                    int locNumber = randomLoc.nextInt(10) + 1;
                    Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." +locNumber + ".world"))),
                            main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                            main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                            main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                    damaged.teleport(location);
                    damaged.setHealth(20);
                    shooter.setHealth(20);
                    arrow.remove();
                    damaged.playSound(Objects.requireNonNull(damaged.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                    damaged.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), "", 0, 20, 10);
                    damaged.setArrowsInBody(0);
                }
        }

        if (event.getDamager() instanceof Zombie) {
            if (event.getEntity() instanceof Player) {
                Player damaged = (Player) event.getEntity();
                int locNumber = randomLoc.nextInt(10) + 1;
                Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." +locNumber + ".world"))),
                        main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                        main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                        main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
                if(damaged.getHealth() <= 1) {
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + damaged.getName() + " &ewas killed by a zombie."));
                    data.addDeaths(damaged.getUniqueId(), 1);
                    if(this.main.map.get(damaged.getUniqueId()).points > 3) {
                        data.addPoints(damaged.getUniqueId(), -3);
                    }
                    event.setCancelled(true);
                    damaged.teleport(location);
                    damaged.playSound(Objects.requireNonNull(damaged.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                    damaged.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), "", 0, 20, 10);
                    damaged.setHealth(20);
                }
            }
        }

    }


}
