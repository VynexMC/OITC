package org.mesmeralis.OITC.managers;

import org.bukkit.Bukkit;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.utils.ColourUtils;

import java.util.HashMap;
import java.util.Objects;

public class MapManager {
    public Main main;
    public GameManager manager;
    public MapManager (Main main) {
        this.main = main;
        this.manager = main.gameManager;
    }

    public HashMap<String, Integer> mapVotes = new HashMap<>();

    public void runMapCheck() {
        if (!main.getConfig().contains("maps")) {
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&cNo other maps found, defaulting to the main map..."));
        } else {
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(manager.prefix + "&eVote for a map."));
            for(String maps : Objects.requireNonNull(main.getConfig().getConfigurationSection("maps")).getKeys(false)) {
                mapVotes.putIfAbsent(maps, 0);
                Bukkit.getServer().broadcastMessage(manager.prefix + "&6" + main.getConfig().get("maps.") );
            }
        }
    }


}
