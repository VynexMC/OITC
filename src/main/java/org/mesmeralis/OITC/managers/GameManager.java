package org.mesmeralis.OITC.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    public String prefix = "&c&lOITC &8\u00BB ";
    public Main main;
    public boolean isGameRunning = false;
    public HashMap<Player, Integer> gameKills = new HashMap<>();

    public GameManager(Main main) {
        this.main = main;
    }

    public void startGame() {
        AtomicInteger counter = new AtomicInteger(6);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        int starting;
        for(Player online : Bukkit.getOnlinePlayers()) {
            gameKills.put(online.getPlayer(), 0);
        }

        starting = scheduler.scheduleSyncRepeatingTask(main, () -> {
            counter.getAndDecrement();
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(prefix + "&eStarting in " + counter));
            for(Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online, Sound.UI_BUTTON_CLICK, 10, 1);
            }
        },0L, 20L);

        scheduler.scheduleSyncDelayedTask(main, () ->{
            scheduler.cancelTask(starting);
            for(Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(ColourUtils.colour("&c&lOne in the Chamber"), "Developed by SecMind", 20, 60, 20);
                online.playSound(online, Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
                main.data.createPlayer(online.getPlayer());
                online.setGameMode(GameMode.ADVENTURE);
            }
            giveItems();
            isGameRunning = true;
            teleport();
        }, 100L);

        scheduler.scheduleSyncDelayedTask(main, () -> {
            Player winner = this.getWinner();
            if(winner != null) {
                for(Player online : Bukkit.getOnlinePlayers()) {
                    online.sendTitle(ColourUtils.colour("&4&lGAME OVER"), ColourUtils.colour("&e" + getWinner().getName() + "&a won the game!"), 20, 60, 20);
                    online.playSound(online, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                    online.getInventory().clear();
                    online.teleport(Objects.requireNonNull(main.getConfig().getLocation("lobby")));
                    online.setGameMode(GameMode.ADVENTURE);
                }
                winner.playSound(winner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 5);
                main.data.addPoints(winner.getUniqueId(), 15);
                main.data.addWins(winner.getUniqueId(), 1);
                gameKills.clear();
                isGameRunning = false;
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(prefix + "&eCongratulations to &a" + winner.getName() + "&e for winning. They have been given &a15 &epoints."));
            } else {
                for(Player online : Bukkit.getOnlinePlayers()) {
                    online.sendTitle(ColourUtils.colour("&4&lGAME OVER"), ColourUtils.colour("&cThere was no winner."), 20, 60, 20);
                    online.playSound(online, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                    online.getInventory().clear();
                    online.teleport(Objects.requireNonNull(main.getConfig().getLocation("lobby")));
                    online.setGameMode(GameMode.ADVENTURE);
                }
                gameKills.clear();
                isGameRunning = false;
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(prefix + "&cThere was no winner for this game."));
            }
        }, 2400L);
    }

    ItemStack gameBow = new ItemStack(Material.BOW, 1);
    ItemStack gameArrow = new ItemStack(Material.ARROW, 1);
    ItemMeta bowMeta = gameBow.getItemMeta();
    public void giveItems() {
        for(Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().clear();
            bowMeta.addEnchant(Enchantment.INFINITY, 1, false);
            gameBow.setItemMeta(bowMeta);
            online.getInventory().setItem(0, gameBow);
            online.getInventory().setItem(1, gameArrow);
        }
    }

    public void teleport() {
        Random randomLoc = new Random();
        for(Player online: Bukkit.getOnlinePlayers()) {
            int locNumber = randomLoc.nextInt(10) + 1;
            online.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn." + locNumber)));
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

}
