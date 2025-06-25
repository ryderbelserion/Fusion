package com.ryderbelserion.fusion.common.api.interfaces.plugins;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public abstract class IPlugin {

    /**
     * Checks if the plugin is enabled.
     *
     * @return true or false
     */
    public boolean isEnabled() {
        return false;
    }

    /**
     * Retrieves the key of the plugin.
     *
     * @return the key of the plugin
     */
    public @NotNull Key getKey() {
        return Key.key("");
    }

    /**
     * Starts the plugin
     *
     * @return {@link IPlugin}
     */
    public @NotNull IPlugin start() {
        return this;
    }

    /**
     * Checks if a player is vanished.
     *
     * @param uuid the uuid to check
     * @return true or false
     */
    public boolean isVanished(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Check if a player is ignoring another player.
     *
     * @param uuid the person doing the ignoring
     * @param target the target who is being ignored
     * @return true or false
     */
    public boolean isIgnored(@NotNull final UUID uuid, @NotNull final UUID target) {
        return false;
    }

    /**
     * Checks if a player is muted.
     *
     * @param uuid the uuid to check
     * @return true or false
     */
    public boolean isMuted(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Checks if a player is afk.
     *
     * @param uuid the uuid to check
     * @return true or false
     */
    public boolean isAfk(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Stops the plugin
     */
    public void stop() {}

    /**
     * A generic getter to get... something, I forgot the use case.
     *
     * @param key the value
     * @return whatever I want
     * @param <V> whatever I want
     * @param <T> whatever I want
     */
    public <V, T> @Nullable T getData(@Nullable final V key) {
        return null;
    }

    /**
     * A generic getter to get... something, I forgot the use case.
     *
     * @return whatever I want
     * @param <T> whatever I want
     */
    public <T> T getData() {
        return getData(null);
    }
}