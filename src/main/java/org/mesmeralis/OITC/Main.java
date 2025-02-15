package org.mesmeralis.OITC;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mesmeralis.OITC.commands.AdminCommand;
import org.mesmeralis.OITC.listeners.PlayerDamageListener;
import org.mesmeralis.OITC.listeners.PlayerListener;
import org.mesmeralis.OITC.listeners.ProjectileHitListener;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.managers.RankManager;
import org.mesmeralis.OITC.papi.PapiExpansion;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.storage.Storage;

import java.sql.SQLException;


public final class Main extends JavaPlugin {

    public Storage storage;
    public SQLGetter data;
    private GameManager gameManager;
    public RankManager rankManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.storage = new Storage(this);
        try {
            storage.connectSQL();
            Bukkit.getLogger().info("Database connected successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info("Database was unable to connect.");
        }
        this.data = new SQLGetter(this);
        this.gameManager = new GameManager(this);
        this.initListeners();
        this.getCommand("admin").setExecutor(new AdminCommand(gameManager, this));
        this.rankManager = new RankManager(this);
        rankManager.loadRanks();
        new PapiExpansion(this).register();
    }

    @Override
    public void onDisable() {
        storage.disconnectSQL();
        Bukkit.getLogger().info("Database disconnected.");
    }


    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(gameManager), this);
        pluginManager.registerEvents(new PlayerDamageListener(this, gameManager, data), this);
        pluginManager.registerEvents(new ProjectileHitListener(this, gameManager), this);
    }



}
