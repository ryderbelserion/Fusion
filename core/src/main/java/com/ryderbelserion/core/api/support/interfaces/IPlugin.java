package com.ryderbelserion.core.api.support.interfaces;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public interface IPlugin {

    default boolean isEnabled() {
        return false;
    }

    default @NotNull String getName() {
        return "";
    }

    default IPlugin init() {
        return this;
    }

    default void stop() {}

    default boolean isVanished(@NotNull final UUID uuid) {
        return false;
    }

    default boolean isIgnored(@NotNull final UUID sender, @NotNull final UUID target) {
        return false;
    }

    default boolean isMuted(@NotNull final UUID uuid) {
        return false;
    }

    default boolean isAfk(@NotNull final UUID uuid) {
        return false;
    }

    default <T> T get() {
        return null;
    }
}