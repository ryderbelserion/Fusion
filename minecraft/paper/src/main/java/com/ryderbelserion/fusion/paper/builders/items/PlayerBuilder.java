package com.ryderbelserion.fusion.paper.builders.items;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class PlayerBuilder {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.api();
    private final Server server = this.fusion.getServer();

    private final String name;

    public PlayerBuilder(final String name) {
        this.name = name;
    }

    public <T> Optional<T> getTarget() {
        if (this.name.isBlank()) {
            return Optional.empty();
        }

        final OfflinePlayer target = CompletableFuture.supplyAsync(() -> this.server.getOfflinePlayer(this.name)).join();

        if (target.isOnline()) {
            final Player player = target.getPlayer();

            if (player == null) {
                return Optional.empty();
            }

            return Optional.of((T) player);
        }

        return Optional.of((T) target);
    }
}