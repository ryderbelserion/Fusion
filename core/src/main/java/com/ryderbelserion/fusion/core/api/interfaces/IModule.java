package com.ryderbelserion.fusion.core.api.interfaces;

public interface IModule {

    default boolean isEnabled() {
        return false;
    }

    default void enable() {}

    default void reload() {}

    default void disable() {}

    String getName();

}