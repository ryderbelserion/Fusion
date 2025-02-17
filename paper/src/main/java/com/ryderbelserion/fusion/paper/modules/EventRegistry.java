package com.ryderbelserion.fusion.paper.modules;

import com.ryderbelserion.fusion.paper.modules.interfaces.IPaperModule;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class EventRegistry {

    private final Plugin plugin;
    private final Server server;

    public EventRegistry(@NotNull final Plugin plugin, @NotNull final Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    public void addEvent(@NotNull final IPaperModule event) {
        this.server.getPluginManager().registerEvents(event, this.plugin);
    }
}