package org.mesmeralis.OITC.papi;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesmeralis.OITC.Main;
import org.mesmeralis.OITC.utils.ColourUtils;

public class PapiExpansion extends PlaceholderExpansion {
    private final Main main;
    public PapiExpansion (Main main) {
        this.main = main;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "oitc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Mesmeralis";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("points")) {
            String points = String.valueOf(this.main.data.getPoints(player.getUniqueId()));
            return ColourUtils.colour(points);
        }
        if (params.equalsIgnoreCase("kills")) {
            String kills = String.valueOf(this.main.data.getKills(player.getUniqueId()));
            return ColourUtils.colour(kills);
        }
        if (params.equalsIgnoreCase("deaths")) {
            String deaths = String.valueOf(this.main.data.getDeaths(player.getUniqueId()));
            return ColourUtils.colour(deaths);
        }
        if (params.equalsIgnoreCase("wins")) {
            String wins = String.valueOf(this.main.data.getWins(player.getUniqueId()));
            return ColourUtils.colour(wins);
        }
        return null;
    }
}
