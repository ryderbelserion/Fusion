package com.ryderbelserion.fusion.core.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IPlugin {

    default boolean isEnabled() {
        return false;
    }

    default @NotNull String getName() {
        return "";
    }

    default @NotNull IPlugin init() {
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

    default <V, T> T getData(@Nullable final V variable) {
        return null;
    }

    default <T> T getData() {
        return getData(null);
    }
}