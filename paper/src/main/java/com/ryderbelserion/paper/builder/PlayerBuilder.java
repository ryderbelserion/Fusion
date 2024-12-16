package com.ryderbelserion.paper.builder;

import com.ryderbelserion.FusionApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record PlayerBuilder(String name) {

    private static final @NotNull Plugin plugin = FusionApi.get().getPlugin();

    private static final @NotNull Server server = plugin.getServer();

    public @Nullable OfflinePlayer getOfflinePlayer() {
        if (this.name.isEmpty()) return null;

        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> server.getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

        return server.getOfflinePlayer(future.join());
    }

    public @Nullable Player getPlayer() {
        if (this.name.isEmpty()) return null;

        return server.getPlayerExact(this.name);
    }
}