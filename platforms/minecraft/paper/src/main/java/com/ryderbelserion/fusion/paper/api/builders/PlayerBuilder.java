package com.ryderbelserion.fusion.paper.api.builders;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record PlayerBuilder(String name) {

    private static @NotNull final Server server = Bukkit.getServer();

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

        Player player = getPlayer();

        if (player == null) {
            PlayerProfile profile = getOfflineProfile();

            if (profile != null) {
                return profile.getId();
            }

            OfflinePlayer offlinePlayer = getOfflinePlayer();

            if (offlinePlayer != null) {
                return offlinePlayer.getUniqueId();
            }

            return null;
        }

        return player.getUniqueId();
    }
}