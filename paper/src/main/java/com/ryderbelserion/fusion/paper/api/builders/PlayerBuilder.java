package com.ryderbelserion.fusion.paper.api.builders;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public record PlayerBuilder(Server server, String name) {

    public @Nullable PlayerProfile getOfflineProfile() {
        if (this.name.isEmpty()) return null;

        final AtomicReference<PlayerProfile> playerProfile = new AtomicReference<>();

        CompletableFuture.supplyAsync(() -> this.server.createProfile(this.name)).thenAccept(playerProfile::set);

        return playerProfile.get();
    }

    public @Nullable OfflinePlayer getOfflinePlayer() {
        if (this.name.isEmpty()) return null;

        final AtomicReference<OfflinePlayer> offlinePlayer = new AtomicReference<>();

        CompletableFuture.supplyAsync(() -> this.server.getOfflinePlayer(this.name)).thenAccept(offlinePlayer::set);

        return offlinePlayer.get();
    }

    public @Nullable Player getPlayer() {
        if (this.name.isEmpty()) return null;

        return this.server.getPlayerExact(this.name);
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