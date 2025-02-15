package org.mesmeralis.OITC.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    public String prefix = "&c&lOITC &8\u00BB ";
    public Main main;
    public boolean isGameRunning = false;
    public SQLGetter data;

    public GameManager(Main main, SQLGetter data) {
        this.main = main;
        this.data = data;
    }

    public void startGame() {
        AtomicInteger counter = new AtomicInteger(6);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        int starting;
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
                online.sendTitle(ColourUtils.colour("&c&lOne in the Chamber"), "Developed by Mesmeralis", 20, 60, 20);
                online.playSound(online, Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
                data.createPlayer(online.getPlayer());
            }
            giveItems();
            isGameRunning = true;
            teleport();
        }, 100L);

        scheduler.scheduleSyncDelayedTask(main, () -> {
            for(Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(ColourUtils.colour("&4&lGAME OVER"), "Thanks for playing!", 20, 60, 20);
                online.playSound(online, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                online.getInventory().clear();
                online.teleport(Bukkit.getServer().getWorld("spawn").getSpawnLocation());
            }
            isGameRunning = false;
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

}
