package com.ryderbelserion.paper.modules;

import com.ryderbelserion.paper.modules.interfaces.IPaperModule;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * An event registry to register events
 *
 * @author ryderbelserion
 * @version 0.16.2
 * @since 0.11.0
 */
public class EventRegistry {

    private final Plugin plugin;
    private final Server server;

    /**
     * Builds the event registry
     * 
     * @param server {@link Server}
     * @since 0.11.0
     */
    public EventRegistry(@NotNull final Plugin plugin, @NotNull final Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    /**
     * Add a listener to the server
     *
     * @param event {@link com.ryderbelserion.core.api.interfaces.IModule}
     * @since 0.11.0
     */
    public void addEvent(@NotNull final IPaperModule event) {
        this.server.getPluginManager().registerEvents(event, this.plugin);
    }
}