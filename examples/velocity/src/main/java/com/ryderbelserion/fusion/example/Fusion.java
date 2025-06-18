package com.ryderbelserion.fusion.example;

import com.google.inject.Inject;
import com.ryderbelserion.fusion.example.commands.BaseCommand;
import com.ryderbelserion.fusion.velocity.FusionVelocity;
import com.ryderbelserion.fusion.velocity.api.commands.VelocityCommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.nio.file.Path;
import java.util.List;

@Plugin(id = "fusion", name = "Fusion", version = "1.0.0", description = "a test plugin!", authors = "ryderbelserion")
public class Fusion {

    private final ProxyServer server;
    private final ComponentLogger logger;
    private final Path path;

    @Inject
    public Fusion(final ProxyServer server, final ComponentLogger logger, @DataDirectory final Path directory) {
        this.server = server;
        this.logger = logger;
        this.path = directory;

        this.fusion = new FusionVelocity(this.logger, this.path);
    }

    private final FusionVelocity fusion;

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        this.fusion.enable(this.server, this, "fusion");

        final VelocityCommandManager commandManager = this.fusion.getCommandManager();

        commandManager.enable(new BaseCommand(this.server), "The base command for Fusion!", List.of());
    }

    public final ProxyServer getServer() {
        return this.server;
    }
}