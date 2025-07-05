package com.ryderbelserion.fusion.core.plugins;

import com.ryderbelserion.fusion.core.api.interfaces.plugins.IPlugin;
import com.ryderbelserion.fusion.core.api.interfaces.plugins.IPluginManager;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles plugin dependencies
 */
public class FusionPluginManager extends IPluginManager {

    private final Map<Key, IPlugin> plugins = new HashMap<>();

    /**
     * Adds an instance of {@link IPlugin} to the cache.
     *
     * @param plugin the instance of {@link IPlugin}
     */
    @Override
    public void addPlugin(@NotNull final IPlugin plugin) {
        this.plugins.put(plugin.getKey(), plugin);
    }

    /**
     * Removes a plugin from the cache.
     *
     * @param key the key of the plugin
     */
    @Override
    public void removePlugin(@NotNull final Key key) {
        this.plugins.remove(key);
    }

    /**
     * Gets a plugin from the cache.
     *
     * @param key the key of the plugin
     * @return {@link IPlugin}
     */
    @Override
    public @Nullable IPlugin getPlugin(@NotNull final Key key) {
        return this.plugins.get(key);
    }
}