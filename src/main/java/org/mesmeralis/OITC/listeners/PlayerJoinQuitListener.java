package org.mesmeralis.OITC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    public static int playersNeeded = 2;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Location lobby = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("lobby.world"))),
                main.getConfig().getInt("lobby.x"), main.getConfig().getInt("lobby.y"), main.getConfig().getInt("lobby.z"),
                main.getConfig().getInt("lobby.yaw"), main.getConfig().getInt("lobby.pitch"));
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        int difference = playersNeeded - onlinePlayers;
        if (!main.gameManager.isGameRunning) {
            event.setJoinMessage(ColourUtils.colour(this.main.gameManager.prefix + "&a" + event.getPlayer().getName() + " joined."));
            Bukkit.getServer().getScheduler().runTaskLater(main, () -> {
                if (onlinePlayers < playersNeeded) {
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(
                            this.main.gameManager.prefix + "&a" + difference + " &emore players are needed to start the game."));
                }
            }, 20L);
            if (main.getConfig().contains("lobby")) {
                event.getPlayer().teleport(lobby);
            }
            else {
                for (Player admin : Bukkit.getOnlinePlayers()) {
                    if (admin.hasPermission("oitc.admin")) {
                        admin.sendMessage(ColourUtils.colour(main.gameManager.prefix + "&cLobby spawn not set."));
                    }
                }
            }
            if (onlinePlayers == playersNeeded) {
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(this.main.gameManager.prefix + "&aEnough players have joined. Game will start in 15 seconds."));
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                    this.main.gameManager.startGame();
                }, 200L);
            }
        }
        else {
            event.setJoinMessage(ColourUtils.colour(this.main.gameManager.prefix + "&a" + event.getPlayer().getName() + " joined as a spectator."));
            setPlayerAsSpectator(event.getPlayer());
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ColourUtils.colour(this.main.gameManager.prefix + "&c" + event.getPlayer().getName() + " left."));
        this.main.map.remove(event.getPlayer().getUniqueId());
        if (Bukkit.getOnlinePlayers().size() - 1 < playersNeeded) {
            if (!main.gameManager.isGameRunning) {
                Bukkit.getServer().getScheduler().cancelTask(main.gameManager.starting);
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.playSound(online.getLocation(), Sound.BLOCK_ANVIL_FALL, 10, 1);
                }
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.gameManager.prefix + "&cNot enough players to start the game."));
            }
            else {
                main.gameManager.startEndingTimer();
                Bukkit.getServer().getScheduler().cancelTask(main.gameManager.endGameTask);
            }
        }
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
        Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn.1.world"))),
                main.getConfig().getInt("gamespawn.1.x"), main.getConfig().getInt("gamespawn.1.y"),
                main.getConfig().getInt("gamespawn.1.z"),
                main.getConfig().getInt("gamespawn.1.yaw"), main.getConfig().getInt("gamespawn.1.pitch"));
        player.sendMessage(ColourUtils.colour(this.main.gameManager.prefix + "&eYou joined while the game was running, you are now a spectator."));
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(location);
        }, 10L);
    }


}
