package com.ryderbelserion.fusion.paper.builders.items;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerBuilder {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();
    private final Server server = this.fusion.getServer();

    private final String name;

    public PlayerBuilder(@NonNull final String name) {
        this.name = name;
    }

    public @NonNull Optional<OfflinePlayer> getOfflinePlayer() {
        if (this.name.isEmpty()) return Optional.empty();

        final CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> this.server.getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

        return Optional.of(CompletableFuture.supplyAsync((() -> this.server.getOfflinePlayer(future.join()))).join());
    }

    public @NonNull Optional<Player> getPlayer() {
        if (this.name.isEmpty()) return Optional.empty();

        return Optional.ofNullable(this.server.getPlayerExact(this.name));
    }
}