package com.ryderbelserion.fusion.core.managers;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.IPlugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {

    private final FusionCore api = FusionCore.Provider.get();

    private final ComponentLogger logger = api.getLogger();

    private final boolean isVerbose = api.isVerbose();

    private final Map<String, IPlugin> plugins = new HashMap<>();

    public void registerPlugin(@NotNull final IPlugin plugin) {
        this.plugins.put(plugin.getName(), plugin.init());
    }

    public @Nullable final IPlugin getPlugin(@NotNull final String name) {
        return this.plugins.get(name);
    }

    public final boolean isEnabled(@NotNull final String name) {
        final IPlugin plugin = getPlugin(name);

        return plugin != null && plugin.isEnabled();
    }

    public void unregisterPlugin(@NotNull final IPlugin plugin) {
        this.plugins.remove(plugin.getName());

        plugin.stop();
    }

    public void printPlugins() {
        if (this.isVerbose) {
            getPlugins().forEach((name, plugin) -> {
                if (plugin.isEnabled() && !name.isEmpty()) {
                    this.logger.info("{}: FOUND", name);
                } else {
                    this.logger.info("{}: NOT FOUND", name);
                }
            });
        }
    }

    public @NotNull final Map<String, IPlugin> getPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }
}