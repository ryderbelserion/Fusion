package com.ryderbelserion.fusion.paper.builders;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerBuilder {

    private final Server server;
    private final String name;

    public PlayerBuilder(@NotNull final JavaPlugin plugin, @NotNull final String name) {
        this.server = plugin.getServer();
        this.name = name;
    }

    public @Nullable OfflinePlayer getOfflinePlayer() {
        if (this.name.isEmpty()) return null;

        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> this.server.getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

        return this.server.getOfflinePlayer(future.join());
    }

    public @Nullable Player getPlayer() {
        if (this.name.isEmpty()) return null;

        return this.server.getPlayerExact(this.name);
    }
}