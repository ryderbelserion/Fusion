package com.ryderbelserion.fusion.core.api.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IModule {

    default boolean isEnabled() {
        return false;
    }

    default void enable() {}

    default void reload() {}

    default void disable() {}

    @NotNull String getName();

}