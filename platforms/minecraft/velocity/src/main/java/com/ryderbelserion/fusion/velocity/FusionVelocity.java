package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.velocity.api.commands.VelocityCommandManager;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class FusionVelocity extends FusionKyori {

    private VelocityCommandManager commandManager;

    public FusionVelocity(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(logger, path);

        load();
    }

    public void enable(@NotNull final ProxyServer server, @NotNull final String rootCommand) {
        this.commandManager = new VelocityCommandManager(server.getCommandManager(), server.getClass(), rootCommand);
    }

    @Override
    public @NotNull final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message) {
        return message;
    }

    @Override
    public @NotNull final String chomp(@NotNull final String message) {
        return message;
    }

    @Override
    public @NotNull final VelocityCommandManager getCommandManager() {
        return this.commandManager;
    }
}