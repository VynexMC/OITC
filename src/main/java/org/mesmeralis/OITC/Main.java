package org.mesmeralis.OITC;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mesmeralis.OITC.commands.AdminCommand;
import org.mesmeralis.OITC.listeners.PlayerListener;
import org.mesmeralis.OITC.managers.GameManager;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private final GameManager gameManager = new GameManager(this);


    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.initListeners();
        this.getCommand("admin").setExecutor(new AdminCommand(gameManager, this));
    }

    @Override
    public void onDisable() {
        saveConfig();
    }


    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(gameManager), this);
    }

}
