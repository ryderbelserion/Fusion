package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.velocity.api.commands.FusionCommandManager;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class FusionVelocity extends FusionCore {

    private FusionCommandManager commandManager;

    public FusionVelocity(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(logger, path);
    }

    public void enable(@NotNull final ProxyServer server, @NotNull final Object fusion, @NotNull final String rootCommand) {
        this.commandManager = new FusionCommandManager(server.getCommandManager(), fusion, rootCommand);
    }

    @Override
    public @NotNull final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message) {
        return message;
    }

    @Override
    public @NotNull final FusionCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull final String chomp(@NotNull final String message) {
        return message;
    }
}