package com.ryderbelserion.fusion.core.api.plugins;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.plugins.interfacers.IPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginBuilder {

    private final FusionCore api = FusionCore.Provider.get();

    private final ILogger logger = this.api.getLogger();

    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, IPlugin> plugins = new HashMap<>();

    public @Nullable final IPlugin getPlugin(@NotNull final String name) {
        return this.plugins.get(name);
    }

    public void registerPlugin(@NotNull final IPlugin plugin) {
        this.plugins.put(plugin.getName(), plugin.start());
    }

    public void unregisterPlugin(@NotNull final IPlugin plugin) {
        this.plugins.remove(plugin.getName());

        plugin.stop();
    }

    public final boolean isEnabled(@NotNull final String name) {
        final IPlugin plugin = getPlugin(name);

        return plugin != null && plugin.isEnabled();
    }

    public @NotNull final Map<String, IPlugin> getPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }

    public void printPlugins() {
        if (this.isVerbose) {
            getPlugins().forEach((name, plugin) -> {
                if (plugin.isEnabled() && !name.isEmpty()) {
                    this.logger.safe("{}: FOUND", name);
                } else {
                    this.logger.safe("{}: NOT FOUND", name);
                }
            });
        }
    }
}