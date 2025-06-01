package com.ryderbelserion.fusion.core.api.plugins;

import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.plugins.interfacers.IPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple way to have generic support for multiple plugins, I type less code.
 */
public class PluginBuilder {

    private final ILogger logger;

    /**
     * The default constructor for {@link PluginBuilder}.
     *
     * @param logger instance of {@link ILogger}
     */
    public PluginBuilder(@NotNull ILogger logger) {
        this.logger = logger;
    }

    private final Map<String, IPlugin> plugins = new HashMap<>();

    /**
     * Retrieves a plugin if it exists.
     *
     * @param name the name of the plugin
     * @return instance of {@link IPlugin}
     */
    public @Nullable final IPlugin getPlugin(@NotNull String name) {
        return this.plugins.get(name);
    }

    /**
     * Registers and adds a plugin to the cache.
     *
     * @param plugin instance of {@link IPlugin}
     */
    public void registerPlugin(@NotNull IPlugin plugin) {
        this.plugins.put(plugin.getName(), plugin.start());

        if (plugin.isEnabled()) {
            this.logger.warn("The plugin {} has successfully enabled.", plugin.getName());
        }
    }

    /**
     * Unregisters a plugin, and removes it from the cache.
     *
     * @param plugin instance of {@link IPlugin}
     */
    public void unregisterPlugin(@NotNull IPlugin plugin) {
        this.plugins.remove(plugin.getName());

        plugin.stop();

        this.logger.warn("The plugin {} has successfully disabled.", plugin.getName());
    }

    /**
     * Checks if a plugin is enabled.
     *
     * @param name the name of the plugin
     * @return true or false
     */
    public final boolean isEnabled(@NotNull String name) {
        IPlugin plugin = getPlugin(name);

        return plugin != null && plugin.isEnabled();
    }

    /**
     * Retrieves an unmodifiable map of {@link IPlugin}.
     *
     * @return the map of plugin data
     */
    public @NotNull final Map<String, IPlugin> getPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }
}