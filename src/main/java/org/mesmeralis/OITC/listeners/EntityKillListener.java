package org.mesmeralis.OITC.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.utils.ColourUtils;

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
    }

}
