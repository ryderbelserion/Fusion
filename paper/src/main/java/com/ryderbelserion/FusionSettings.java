package com.ryderbelserion;

import com.ryderbelserion.exception.FusionException;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FusionSettings {

    private static final FusionSettings instance = new FusionSettings();

    /**
     * Fetch the static instance of FusionSettings.
     *
     * @return {@link FusionSettings}
     */
    public static FusionSettings get() {
        return instance;
    }

    private boolean isRegistered;
    private Plugin plugin = null;

    /**
     * Register the plugin instance, along with any listeners.
     *
     * @param plugin {@link Plugin}
     */
    public void register(@NotNull Plugin plugin) {
        if (this.isRegistered) return;

        this.isRegistered = true;
        this.plugin = plugin;

        final Server server = this.plugin.getServer();

        // Register any listeners
    }

    /**
     * Gets the instance of {@link Plugin}.
     *
     * @return {@link Plugin}
     */
    public @NotNull Plugin getPlugin() {
        if (this.plugin == null) {
            throw new FusionException("An error occurred while trying to get the plugin instance.");
        }

        return this.plugin;
    }
}