package org.mesmeralis.OITC.papi;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PapiExpansion extends PlaceholderExpansion {
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
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return null;
    }
}
