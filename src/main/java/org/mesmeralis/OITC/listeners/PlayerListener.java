package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.papi.PapiExpansion;
import org.mesmeralis.OITC.utils.ColourUtils;

public class PlayerListener implements Listener {

    public Main main;
    public PlayerListener( Main main) {
        this.main = main;
    }

    int maxPlayers = 10;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        event.setJoinMessage(ColourUtils.colour(this.main.gameManager.prefix + "&a" + event.getPlayer().getName() + " joined. &eThere are now &a" + onlinePlayers + "&8/&a" + maxPlayers + "&e players online." ));
        if(onlinePlayers == maxPlayers) {
            this.main.gameManager.startGame();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        event.setQuitMessage(ColourUtils.colour(this.main.gameManager.prefix + "&c" + event.getPlayer().getName() + " left. &eThere are now &a" + onlinePlayers + "&8/&a" + maxPlayers + "&e players online." ));
        this.main.map.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onAsyncPlayerJoin(AsyncPlayerPreLoginEvent event) {
        final int points = this.main.data.getPoints(event.getUniqueId()).join();
        final int kills = this.main.data.getKills(event.getUniqueId()).join();
        final int wins = this.main.data.getWins(event.getUniqueId()).join();
        final int deaths = this.main.data.getDeaths(event.getUniqueId()).join();
        PapiExpansion.Record record = new PapiExpansion.Record(points, kills, deaths, wins);
        this.main.map.put(event.getUniqueId(), record);
    }

}
