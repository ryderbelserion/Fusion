package com.ryderbelserion.fusion.paper.api.builders;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.paper.FusionPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record PlayerBuilder(String name) {

    private static @NotNull final Plugin plugin = FusionPlugin.getPlugin();

    private static @NotNull final Server server = plugin.getServer();

    public @Nullable PlayerProfile getOfflineProfile() {
        if (this.name.isEmpty()) return null;

        return CompletableFuture.supplyAsync(() -> server.createProfile(this.name)).join();
    }

    public @Nullable OfflinePlayer getOfflinePlayer() {
        if (this.name.isEmpty()) return null;

        return CompletableFuture.supplyAsync(() -> server.getOfflinePlayer(this.name)).join();
    }

    public @Nullable Player getPlayer() {
        if (this.name.isEmpty()) return null;

        return server.getPlayerExact(this.name);
    }

    public @Nullable UUID getUniqueId() {
        if (this.name.isEmpty()) return null;

        final Player player = getPlayer();

        if (player == null) {
            final PlayerProfile profile = getOfflineProfile();

            if (profile != null) {
                return profile.getId();
            }

            final OfflinePlayer offlinePlayer = getOfflinePlayer();

            if (offlinePlayer != null) {
                return offlinePlayer.getUniqueId();
            }

            return null;
        }

        return player.getUniqueId();
    }
}