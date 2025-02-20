package org.mesmeralis.OITC.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;

public class ProjectileHitListener implements Listener {

    public Main main;
    public GameManager manager;

    public ProjectileHitListener(Main main) {
        this.main = main;
        this.manager = main.gameManager;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (manager.isGameRunning) {
           event.getEntity().remove();
        }
    }


}
