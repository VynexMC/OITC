package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.papi.PapiExpansion;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.Objects;

public class PlayerJoinQuitListener implements Listener {

    public Main main;
    public PlayerJoinQuitListener(Main main) {
        this.main = main;
    }

    int playersNeeded = 2;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Location lobby = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("lobby.world"))),
                main.getConfig().getInt("lobby.x"), main.getConfig().getInt("lobby.y"), main.getConfig().getInt("lobby.z"),
                main.getConfig().getInt("lobby.yaw"), main.getConfig().getInt("lobby.pitch"));
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        if(!main.gameManager.isGameRunning) {
            event.setJoinMessage(ColourUtils.colour(this.main.gameManager.prefix + "&a" + event.getPlayer().getName() + " joined."));
            if(main.getConfig().contains("lobby")) {
                event.getPlayer().teleport(lobby);
            } else {
                for (Player admin : Bukkit.getOnlinePlayers()) {
                    if(admin.hasPermission("oitc.admin")) {
                        admin.sendMessage(ColourUtils.colour(main.gameManager.prefix + "&cLobby spawn not set."));
                    }
                }
            }
            if(onlinePlayers == playersNeeded) {
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(this.main.gameManager.prefix + "&aEnough players have joined. Game will start in 15 seconds."));
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                    this.main.gameManager.startGame();
                }, 200L);
            }
        } else {
            event.setJoinMessage(ColourUtils.colour(this.main.gameManager.prefix + "&a" + event.getPlayer().getName() + " joined as a spectator."));
            setPlayerAsSpectator(event.getPlayer());
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ColourUtils.colour(this.main.gameManager.prefix + "&c" + event.getPlayer().getName() + " left."));
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

    private void setPlayerAsSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(ColourUtils.colour(this.main.gameManager.prefix + "&eYou joined while the game was running, you are now a spectator."));
        player.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn.1")));
    }

}
