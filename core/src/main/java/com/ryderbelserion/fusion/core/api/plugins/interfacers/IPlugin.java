package com.ryderbelserion.fusion.core.api.plugins.interfacers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public interface IPlugin {

    /**
     * Checks if the plugin is enabled.
     *
     * @return true or false
     */
    default boolean isEnabled() {
        return false;
    }

    /**
     * Retrieves the name of the plugin.
     *
     * @return the name of the plugin
     */
    default @NotNull String getName() {
        return "";
    }

    /**
     * Starts the plugin
     *
     * @return {@link IPlugin}
     */
    default @NotNull IPlugin start() {
        return this;
    }

    /**
     * Checks if a player is vanished.
     *
     * @param uuid the uuid to check
     * @return true or false
     */
    default boolean isVanished(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Check if a player is ignoring another player.
     *
     * @param uuid the person doing the ignoring
     * @param target the target who is being ignored
     * @return true or false
     */
    default boolean isIgnored(@NotNull final UUID uuid, @NotNull final UUID target) {
        return false;
    }

    /**
     * Checks if a player is muted.
     *
     * @param uuid the uuid to check
     * @return true or false
     */
    default boolean isMuted(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Checks if a player is afk.
     *
     * @param uuid the uuid to check
     * @return true or false
     */
    default boolean isAfk(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Stops the plugin
     */
    default void stop() {}

    /**
     * A generic getter to get... something, I forgot the use case.
     *
     * @param key the value
     * @return whatever I want
     * @param <V> whatever I want
     * @param <T> whatever I want
     */
    default <V, T> T getData(@Nullable final V key) {
        return null;
    }

    /**
     * A generic getter to get... something, I forgot the use case.
     *
     * @return whatever I want
     * @param <T> whatever I want
     */
    default <T> T getData() {
        return getData(null);
    }
}