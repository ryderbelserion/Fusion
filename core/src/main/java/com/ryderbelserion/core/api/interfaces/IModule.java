package com.ryderbelserion.core.api.interfaces;

/**
 * An abstract class to define what a module can do.
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.10.0
 */
public interface IModule {

    /**
     * Check if the module is enabled.
     *
     * @return true or false
     * @since 0.10.0
     */
    default boolean isEnabled() {
        return false;
    }

    /**
     * Enables the module.
     *
     * @since 0.10.0
     */
    default void enable() {}

    /**
     * Reload the module.
     *
     * @since 0.10.0
     */
    default void reload() {}

    /**
     * Disables the module.
     *
     * @since 0.10.0
     */
    default void disable() {}

    /**
     * Get the name of the module.
     *
     * @return the name of the module
     * @since 0.10.0
     */
    String getName();

}