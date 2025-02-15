package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.utils.ColourUtils;

public class PlayerListener implements Listener {

    private GameManager gameManager;
    public PlayerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
    int maxPlayers = 10;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ColourUtils.colour(gameManager.prefix + "&a" + event.getPlayer().getName() + " joined. &eThere are now &a" + onlinePlayers + "&8/&a" + maxPlayers + "&e players online." ));
        if(onlinePlayers == maxPlayers) {
            gameManager.startGame();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ColourUtils.colour(gameManager.prefix + "&c" + event.getPlayer().getName() + " left. &eThere are now &a" + onlinePlayers + "&8/&a" + maxPlayers + "&e players online." ));
    }

}
