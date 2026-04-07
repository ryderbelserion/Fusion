package com.ryderbelserion.fusion.paper.builders.items;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerBuilder {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();
    private final Server server = this.fusion.getServer();

    private final String name;

    public PlayerBuilder(@NotNull final String name) {
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