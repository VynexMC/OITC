package org.mesmeralis.OITC.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.utils.ColourUtils;

public class SetStatsCommand implements CommandExecutor {

    public Main main;
    public GameManager manager;
    public SQLGetter data;
    public SetStatsCommand(Main main) {
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(cmd.getLabel().equalsIgnoreCase("setstats")) {
            if(args.length == 0 || args.length == 1 || args.length == 2) {
                player.sendMessage(ColourUtils.colour(manager.prefix + "&cUsage is /setstats [player] [points/kills/wins/deaths] [value]"));
            }
            if(args.length == 3) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if(target.hasPlayedBefore()) {
                    int value = Integer.parseInt(args[2]);
                    switch(args[1]) {
                        case "points":
                            data.addPoints(target.getUniqueId(), value - main.map.get(target.getUniqueId()).points);
                            player.sendMessage(ColourUtils.colour(manager.prefix + "&aSet the points of &e" + target.getName() + "&a to &e" + value));
                            break;
                        case "kills":
                            data.addKills(target.getUniqueId(), value - main.map.get(target.getUniqueId()).kills);
                            player.sendMessage(ColourUtils.colour(manager.prefix + "&aSet the kills of &e" + target.getName() + "&a to &e" + value));
                            break;
                        case "deaths":
                            data.addDeaths(target.getUniqueId(), value - main.map.get(target.getUniqueId()).deaths);
                            player.sendMessage(ColourUtils.colour(manager.prefix + "&aSet the deaths of &e" + target.getName() + "&a to &e" + value));
                            break;
                        case "wins":
                            data.addWins(target.getUniqueId(), value - main.map.get(target.getUniqueId()).wins);
                            player.sendMessage(ColourUtils.colour(manager.prefix + "&aSet the wins of &e" + target.getName() + "&a to &e" + value));
                            break;
                    }
                } else {
                    player.sendMessage(ColourUtils.colour(manager.prefix + "&cThat player has never played before. Unable to set their stats."));
                }
            }
        }
        return true;
    }
}
