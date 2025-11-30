package com.ryderbelserion.fusion.addons.interfaces;

import com.ryderbelserion.fusion.addons.AddonClassLoader;
import org.jetbrains.annotations.NotNull;

/**
 * The addon class, handles all things related to addons.
 */
public abstract class IAddon {

    private final AddonClassLoader loader;
    private final String name;

    /**
     * The addon class, handles all things related to addons.
     */
    public IAddon(@NotNull final String name, @NotNull final AddonClassLoader loader, @NotNull final String group) {
        this.name = name;
        this.loader = loader;
    }

    private boolean isEnabled;

    public void onEnable() {}
    public void onDisable() {}
    public void onReload() {}

    /**
     * Enables an addon, this includes adding it to the class path.
     */
    public void enable() {
        if (this.isEnabled()) {
            throw new IllegalStateException("Cannot enable the addon when it's already enabled");
        }

        this.onEnable();
    }

    /**
     * Disables the addon, this includes removing it from the class path.
     */
    public void disable() {
        if (!this.isEnabled()) {
            throw new IllegalStateException("Cannot disable the addon when it's not enabled");
        }

        this.onDisable();
    }

    /**
     * Retrieves an instance of the addon class loader.
     *
     * @return {@link AddonClassLoader}
     */
    public @NotNull AddonClassLoader getLoader() {
        return this.loader;
    }

    /**
     * Sets the addon to be active.
     *
     * @param enabled true or false
     */
    public void setEnabled(final boolean enabled) {
        this.isEnabled = enabled;
    }

    /**
     * Checks if the addon is enabled or not.
     *
     * @return true or false
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Retrieves the name of the addon.
     *
     * @return the name of the addon
     */
    public @NotNull String getName() {
        return this.name;
    }
}