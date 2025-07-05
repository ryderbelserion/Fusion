package com.ryderbelserion.fusion.core.api.interfaces.plugins;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles plugin dependencies
 */
public abstract class IPluginManager {

    /**
     * Adds an instance of {@link IPlugin} to the cache.
     *
     * @param plugin the instance of {@link IPlugin}
     */
    public abstract void addPlugin(@NotNull final IPlugin plugin);

    /**
     * Removes a plugin from the cache.
     *
     * @param key the key of the plugin
     */
    public abstract void removePlugin(@NotNull final Key key);

    /**
     * Gets a plugin from the cache.
     *
     * @param key the key of the plugin
     * @return {@link IPlugin}
     */
    public abstract @Nullable IPlugin getPlugin(@NotNull final Key key);

}