package org.mesmeralis.OITC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mesmeralis.OITC.commands.AdminCommand;
import org.mesmeralis.OITC.commands.SetStatsCommand;
import org.mesmeralis.OITC.commands.StatsCommand;
import org.mesmeralis.OITC.listeners.*;
import org.mesmeralis.OITC.managers.GameManager;
import org.mesmeralis.OITC.managers.RankManager;
import org.mesmeralis.OITC.papi.PapiExpansion;
import org.mesmeralis.OITC.storage.SQLGetter;
import org.mesmeralis.OITC.storage.Storage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;


public final class Main extends JavaPlugin {

    public Storage storage;
    public SQLGetter data;
    public GameManager gameManager;
    public RankManager rankManager;
    public HashMap<UUID, PapiExpansion.Record> map = new HashMap<UUID, PapiExpansion.Record>();
    public UUID topWins;
    public UUID topPoints;

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
        this.getCommand("stats").setExecutor(new StatsCommand(this));
        this.getCommand("setstats").setExecutor(new SetStatsCommand(this));
        this.rankManager = new RankManager(this);
        rankManager.loadRanks();
        new PapiExpansion(this).register();
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            topWins = data.getTopWins().join();
            topPoints = data.getTopPoints().join();
        }, 0L,600L);
        for (Player player : Bukkit.getOnlinePlayers()) {
            final int points = this.data.getPoints(player.getUniqueId()).join();
            final int kills = this.data.getKills(player.getUniqueId()).join();
            final int wins = this.data.getWins(player.getUniqueId()).join();
            final int deaths = this.data.getDeaths(player.getUniqueId()).join();
            PapiExpansion.Record record = new PapiExpansion.Record(points, kills, deaths, wins);
            this.map.put(player.getUniqueId(), record);
        }
    }

    @Override
    public void onDisable() {
        storage.disconnectSQL();
        Bukkit.getLogger().info("Database disconnected.");
    }


    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinQuitListener(this), this);
        pluginManager.registerEvents(new PlayerDamageListener(this), this);
        pluginManager.registerEvents(new ProjectileHitListener(this), this);
        pluginManager.registerEvents(new EntityKillListener(this), this);
        pluginManager.registerEvents(new InventoryListener(this), this);
    }



}
