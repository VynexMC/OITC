package org.mesmeralis.OITC.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.utils.ColourUtils;

public class StatsCommand implements CommandExecutor {

    public Main main;
    public GameManager manager;
    public SQLGetter data;
    public StatsCommand(Main main){
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(cmd.getLabel().equalsIgnoreCase("stats")) {
            if(args.length == 0) {
                player.sendMessage(ColourUtils.colour(manager.prefix + "&7Your stats:"));
                player.sendMessage(ColourUtils.colour(manager.prefix + "&7Points: &e" + main.map.get(player.getUniqueId()).points));
                player.sendMessage(ColourUtils.colour(manager.prefix + "&7Kills: &a" + main.map.get(player.getUniqueId()).kills));
                player.sendMessage(ColourUtils.colour(manager.prefix + "&7Deaths: &c" + main.map.get(player.getUniqueId()).deaths));
                player.sendMessage(ColourUtils.colour(manager.prefix + "&7Wins: &b" + main.map.get(player.getUniqueId()).wins));
            }
            if(args.length == 1) {
                Player target = (Player) Bukkit.getServer().getPlayer(args[0]);
                if(target.hasPlayedBefore()) {
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&a" + target.getName() + "'s &7stats:"));
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&7Points: &e" + main.map.get(target.getUniqueId()).points));
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&7Kills: &a" + main.map.get(target.getUniqueId()).kills));
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&7Deaths: &c" + main.map.get(target.getUniqueId()).deaths));
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&7Wins: &b" + main.map.get(target.getUniqueId()).wins));
                } else {
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&cThis player has never played before, so they have no stats."));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                }
            }
        }


        return true;
    }
}
