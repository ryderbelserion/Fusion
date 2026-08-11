package com.ryderbelserion.fusion.paper.builders.items;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class PlayerBuilder {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.api();
    private final Server server = this.fusion.getServer();

    private final String name;

    public PlayerBuilder(final String name) {
        this.name = name;
    }

    public Optional<OfflinePlayer> getOfflinePlayer() {
        if (this.name.isEmpty()) return Optional.empty();

        final CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> this.server.getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

        return Optional.of(CompletableFuture.supplyAsync((() -> this.server.getOfflinePlayer(future.join()))).join());
    }

    public Optional<Player> getPlayer() {
        if (this.name.isEmpty()) return Optional.empty();

        return Optional.ofNullable(this.server.getPlayerExact(this.name));
    }
}