package com.ryderbelserion.fusion.core.api.events.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IEvent {

    default boolean isEnabled() {
        return false;
    }

    default void start() {}

    default void restart() {}

    default void stop() {}

    @NotNull String getName();

}