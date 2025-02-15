package org.mesmeralis.OITC;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mesmeralis.OITC.commands.AdminCommand;
import org.mesmeralis.OITC.listeners.PlayerListener;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.storage.Storage;

import java.sql.SQLException;


public final class Main extends JavaPlugin {

    private final GameManager gameManager = new GameManager(this);
    public Storage storage;
    public SQLGetter data;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.storage = new Storage(this);
        this.data = new SQLGetter(this);
        try {
            storage.connectSQL();
            Bukkit.getLogger().info("Database connected successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info("Database was unable to connect.");
        }
        this.initListeners();
        this.getCommand("admin").setExecutor(new AdminCommand(gameManager, this));
    }

    @Override
    public void onDisable() {
        storage.disconnectSQL();
    }


    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(gameManager), this);
    }



}
