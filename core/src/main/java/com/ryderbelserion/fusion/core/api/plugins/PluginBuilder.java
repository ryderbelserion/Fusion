package com.ryderbelserion.fusion.core.api.plugins;

import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.plugins.interfacers.IPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginBuilder {

    private final ILogger logger;

    public PluginBuilder(@NotNull final ILogger logger) {
        this.logger = logger;
    }

    private final Map<String, IPlugin> plugins = new HashMap<>();

    public @Nullable final IPlugin getPlugin(@NotNull final String name) {
        return this.plugins.get(name);
    }

    public void registerPlugin(@NotNull final IPlugin plugin) {
        this.plugins.put(plugin.getName(), plugin.start());

        if (plugin.isEnabled()) {
            this.logger.warn("The plugin {} has successfully enabled.", plugin.getName());
        }
    }

    public void unregisterPlugin(@NotNull final IPlugin plugin) {
        this.plugins.remove(plugin.getName());

        plugin.stop();

        this.logger.warn("The plugin {} has successfully disabled.", plugin.getName());
    }

    public final boolean isEnabled(@NotNull final String name) {
        final IPlugin plugin = getPlugin(name);

        return plugin != null && plugin.isEnabled();
    }

    public @NotNull final Map<String, IPlugin> getPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }
}