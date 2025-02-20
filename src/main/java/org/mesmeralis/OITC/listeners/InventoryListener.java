package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.Objects;

public class InventoryListener implements Listener {

    public Main main;
    public GameManager manager;
    public InventoryListener(Main main) {
        this.main = main;
        this.manager = main.gameManager;
    }

    @EventHandler
    public void onInventoryListen(InventoryInteractEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(manager.isGameRunning) {
            if(!manager.eliminated.contains(event.getWhoClicked())) {
                event.setCancelled(true);
                player.sendMessage(ColourUtils.colour(manager.prefix + "&cYou can't do this while a game is running."));
            } else {
                if(player.getItemOnCursor() == manager.lobbyItem) {
                    Location lobby = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("lobby.world"))),
                            main.getConfig().getInt("lobby.x"), main.getConfig().getInt("lobby.y"), main.getConfig().getInt("lobby.z"),
                            main.getConfig().getInt("lobby.yaw"), main.getConfig().getInt("lobby.pitch"));
                    player.teleport(lobby);
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&aYou were teleported to the lobby."));
                    player.getInventory().clear();
                }
            }
        }
    }

}
