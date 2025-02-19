package org.mesmeralis.OITC.managers;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.listeners.PlayerJoinQuitListener;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    public String prefix = "&8–&c&lOITC&8– ";
    public Main main;
    public boolean isGameRunning = false;
    public HashMap<Player, Integer> gameKills = new HashMap<>();

    public GameManager(Main main) {
        this.main = main;
    }

    public int starting;

    public void startGame() {
        AtomicInteger counter = new AtomicInteger(6);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        for (Player online : Bukkit.getOnlinePlayers()) {
            gameKills.put(online.getPlayer(), 0);
        }
        starting = scheduler.scheduleSyncRepeatingTask(main, () -> {
            int count = counter.getAndDecrement();
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(prefix + "&eStarting in " + counter));
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online, Sound.UI_BUTTON_CLICK, 10, 1);
            }
            if (count <= 1) {
                runGame();
            }
        }, 0L, 20L);
    }

    ItemStack gameBow = new ItemStack(Material.BOW, 1);
    ItemStack gameArrow = new ItemStack(Material.ARROW, 1);
    ItemMeta bowMeta = gameBow.getItemMeta();

    public void giveItems() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().clear();
            bowMeta.addEnchant(Enchantment.INFINITY, 1, false);
            gameBow.setItemMeta(bowMeta);
            online.getInventory().setItem(0, gameBow);
            online.getInventory().setItem(1, gameArrow);
        }
    }

    public void teleport() {
        Random randomLoc = new Random();
        Location location;
        for (Player online : Bukkit.getOnlinePlayers()) {
            int locNumber = randomLoc.nextInt(10) + 1;
            location = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("gamespawn." + locNumber + ".world"))),
                    main.getConfig().getInt("gamespawn." + locNumber + ".x"), main.getConfig().getInt("gamespawn." + locNumber + ".y"),
                    main.getConfig().getInt("gamespawn." + locNumber + ".z"),
                    main.getConfig().getInt("gamespawn." + locNumber + ".yaw"), main.getConfig().getInt("gamespawn." + locNumber + ".pitch"));
            online.teleport(location);
        }
    }

    public Player getWinner() {
        int highest = 0;
        Player winner = null;
        for (Player online : Bukkit.getOnlinePlayers()) {
            int amount = this.gameKills.getOrDefault(online.getPlayer(), 0);
            if (highest < amount) {
                highest = amount;
                winner = online;
            }
        }
        return winner;
    }

    public int endGameTask;

    private void runGame() {
        Bukkit.getServer().getScheduler().cancelTask(starting);
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendTitle(ColourUtils.colour("&c&lOne in the Chamber"), "Developed by SecMind", 20, 60, 20);
            online.playSound(online, Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
            main.data.createPlayer(online.getPlayer());
            online.setGameMode(GameMode.ADVENTURE);
        }
        giveItems();
        teleport();
        isGameRunning = true;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        endGameTask = scheduler.scheduleSyncDelayedTask(main, this::startEndingTimer, 2400L);
    }

    public void startEndingTimer() {
        Player winner = this.getWinner();
        Location lobby = new Location(Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("lobby.world"))),
                main.getConfig().getInt("lobby.x"), main.getConfig().getInt("lobby.y"), main.getConfig().getInt("lobby.z"),
                main.getConfig().getInt("lobby.yaw"), main.getConfig().getInt("lobby.pitch"));
        if (winner != null) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(ColourUtils.colour("&4&lGAME OVER"), ColourUtils.colour("&e" + getWinner().getName() + "&a won the game!"), 20, 60, 20);
                online.playSound(online, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                online.getInventory().clear();
                online.teleport(lobby);
                online.setGameMode(GameMode.ADVENTURE);
            }
            winner.playSound(winner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 5);
            main.data.addPoints(winner.getUniqueId(), 15);
            main.data.addWins(winner.getUniqueId(), 1);
            gameKills.clear();
            isGameRunning = false;
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(prefix + "&eCongratulations to &a" + winner.getName() + "&e for winning. They have been given &a15 &epoints."));
        }
        else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(ColourUtils.colour("&4&lGAME OVER"), ColourUtils.colour("&cThere was no winner."), 20, 60, 20);
                online.playSound(online, Sound.BLOCK_ANVIL_FALL, 10, 1);
                online.getInventory().clear();
                if (main.getConfig().contains("lobby")) {
                    online.teleport(lobby);
                }
                else {
                    Bukkit.getServer().broadcastMessage(prefix + "&cLobby spawn not set, could not teleport players.");
                }
                online.setGameMode(GameMode.ADVENTURE);
            }
            gameKills.clear();
            isGameRunning = false;
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(prefix + "&cThere was no winner for this game."));
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.gameManager.prefix + "&aThe next game will begin in 2 minutes."));
        }, 200L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
            if(Bukkit.getOnlinePlayers().size() >= PlayerJoinQuitListener.playersNeeded) {
                startGame();
            }
        }, 2400L);

    }


}
