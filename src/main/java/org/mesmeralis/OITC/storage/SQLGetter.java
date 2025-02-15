package org.mesmeralis.OITC.storage;

import org.bukkit.entity.Player;
import org.mesmeralis.OITC.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {

    private Main main;
    public SQLGetter(Main main) {
        this.main = main;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = main.storage.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS oitc "
            + "(NAME VARCHAR(100), UUID VARCHAR(100), KILLS INT(100), DEATHS INT(100), WINS INT(100), POINTS INT(100), PRIMARY KEY(NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if(!exists(uuid)) {
                PreparedStatement ps2 = main.storage.getConnection().prepareStatement("INSERT IGNORE INTO oitc (NAME,UUID) VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT * FROM oitc WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch(SQLException e) {
            e.printStackTrace();;
        }
        return false;
    }

    public void addPoints(UUID uuid, int points) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE oitc SET POINTS=? WHERE UUID =?");
            ps.setInt(1, getPoints(uuid) + points);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();;
        }
    }

    public void addWins(UUID uuid, int wins) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE oitc SET WINS=? WHERE UUID =?");
            ps.setInt(1, getWins(uuid) + wins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();;
        }
    }

    public void addDeaths(UUID uuid, int deaths) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE oitc SET DEATHS=? WHERE UUID =?");
            ps.setInt(1, getDeaths(uuid) + deaths);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();;
        }
    }

    public void addKills(UUID uuid, int kills) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE oitc SET KILLS=? WHERE UUID =?");
            ps.setInt(1, getKills(uuid) + kills);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();;
        }
    }

    public int getPoints(UUID uuid) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT POINTS FROM oitc WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int points = 0;
            if (rs.next()) {
                points = rs.getInt(points);
                return points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getKills(UUID uuid) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT KILLS FROM oitc WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int kills = 0;
            if (rs.next()) {
                kills = rs.getInt(kills);
                return kills;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(UUID uuid) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT DEATHS FROM oitc WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int deaths = 0;
            if (rs.next()) {
                deaths = rs.getInt(deaths);
                return deaths;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWins(UUID uuid) {
        try {
            PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT WINS FROM oitc WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int wins = 0;
            if (rs.next()) {
                wins = rs.getInt(wins);
                return wins;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
