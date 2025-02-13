package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.mesmeralis.OITC.managers.GameManager;

import java.util.Objects;

public class PlayerDamageListener implements Listener {

    public GameManager manager;
    public PlayerDamageListener (GameManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
           if(manager.isGameRunning) {
               event.setCancelled(true);
           }
        }
    }

    @EventHandler
    public void onKill(EntityDamageEvent event) {
        Player damaged = (Player) event.getEntity();
        if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            Projectile arrow = (Projectile) event.getDamageSource().getCausingEntity();
            Player shooter = (Player) Objects.requireNonNull(arrow).getShooter();
            damaged.setHealth(0);
            Bukkit.getServer().broadcastMessage(manager.prefix + "&c" + damaged.getName() + "&e was killed by &a" + shooter.getName() + "&e.");
        }
    }

}
