package org.mesmeralis.OITC.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.utils.ColourUtils;

public class AdminCommand implements CommandExecutor {

    public GameManager manager;
    public Main main;
    public AdminCommand(GameManager manager, Main main) {
        this.manager = manager;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(cmd.getLabel().equalsIgnoreCase("admin")) {
            if(args.length == 0) {
                return false;
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("start")) {
                    manager.startGame();
                    sender.sendMessage(ColourUtils.colour(manager.prefix + "&aCommand issued successfully."));
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&eThe game was force started by &a" + player.getName() + "&e."));
                }
                if(args[0].equalsIgnoreCase("setgamespawn")) {
                    sender.sendMessage(ColourUtils.colour(manager.prefix + "&c/admin setgamespawn [1-10]"));
                }
                if(args[0].equalsIgnoreCase("setpoints")) {
                    sender.sendMessage(ColourUtils.colour(manager.prefix + "/admin setpoints [user] [points]"));
                }
                if(args[0].equalsIgnoreCase("setlobby")) {
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&aSet the waiting lobby spawn to your location."));
                   // main.getConfig().set("lobby", player.getLocation());
                    main.getConfig().set("lobby.world", player.getWorld().getName());
                    main.getConfig().set("lobby.x", player.getLocation().getX());
                    main.getConfig().set("lobby.y", player.getLocation().getY());
                    main.getConfig().set("lobby.z", player.getLocation().getZ());
                    main.getConfig().set("lobby.pitch", player.getLocation().getPitch());
                    main.getConfig().set("lobby.yaw", player.getLocation().getYaw());
                    main.saveConfig();
                }
            }
            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("setgamespawn")) {
                    switch(args[1]) {
                        case "1":
                            main.getConfig().set("gamespawn.1.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.1.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.1.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.1.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.1.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.1.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "2":
                            main.getConfig().set("gamespawn.2.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.2.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.2.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.2.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.2.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.2.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "3":
                            main.getConfig().set("gamespawn.3.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.3.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.3.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.3.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.3.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.3.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "4":
                            main.getConfig().set("gamespawn.4.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.4.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.4.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.4.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.4.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.4.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "5":
                            main.getConfig().set("gamespawn.5.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.5.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.5.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.5.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.5.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.5.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "6":
                            main.getConfig().set("gamespawn.6.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.6.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.6.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.6.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.6.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.6.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "7":
                            main.getConfig().set("gamespawn.7.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.7.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.7.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.7.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.7.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.7.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "8":
                            main.getConfig().set("gamespawn.8.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.8.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.8.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.8.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.8.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.8.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "9":
                            main.getConfig().set("gamespawn.9.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.9.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.9.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.9.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.9.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.9.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        case "10":
                            main.getConfig().set("gamespawn.10.world", player.getWorld().getName());
                            main.getConfig().set("gamspawn.10.x", player.getLocation().getX());
                            main.getConfig().set("gamespawn.10.y", player.getLocation().getY());
                            main.getConfig().set("gamespawn.10.z", player.getLocation().getZ());
                            main.getConfig().set("gamespawn.10.pitch", player.getLocation().getPitch());
                            main.getConfig().set("gamespawn.10.yaw", player.getLocation().getYaw());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&aGame spawn set successfully."));
                            break;
                        default:
                            sender.sendMessage(ColourUtils.colour(manager.prefix + "&cIncorrect spawn value."));
                            break;
                    }
                }
            }
        }

        return true;
    }
}
