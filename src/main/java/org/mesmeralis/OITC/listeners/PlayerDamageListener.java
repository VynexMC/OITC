package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
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
    public PlayerDamageListener (Main main, GameManager manager, SQLGetter data) {
        this.main = main;
        this.manager = manager;
        this.data = data;
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
                    Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&c" + damaged.getName() + "&e was shot by &a" + shooter.getName() + "&e."));
                    data.addKills(shooter.getUniqueId(), 1);
                    data.addDeaths(damaged.getUniqueId(), 1);
                    int locNumber = randomLoc.nextInt(10) + 1;
                    damaged.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn." + locNumber)));
                    damaged.setHealth(20);
                    damaged.playSound(Objects.requireNonNull(damaged.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                    damaged.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), "", 0, 20, 10);
                }
        }


    }


}
