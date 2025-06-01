package com.ryderbelserion.fusion.core.api.addons.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.addons.AddonClassLoader;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

/**
 * The addon class, handles all things related to addons.
 */
public abstract class IAddon {

    protected final FusionCore fusion = FusionCore.Provider.get();

    private AddonClassLoader loader;
    private boolean isEnabled;
    private String name;
    private Path folder;

    /**
     * Enables the addon.
     */
    public abstract void onEnable();

    /**
     * Disables the addon.
     */
    public abstract void onDisable();

    /**
     * Gets the logger instance
     *
     * @return {@link ILogger}
     */
    public ILogger getLogger() {
        return this.fusion.getLogger();
    }

    /**
     * Enables an addon, this includes adding it to the class path.
     *
     * @param folder the addon's folder
     */
    public void enable(@NotNull Path folder) {
        if (this.isEnabled()) {
            throw new FusionException("Cannot enable the addon when it's already enabled");
        }

        this.folder = folder;

        this.onEnable();
        this.setEnabled(true);
    }

    /**
     * Disables the addon, this includes removing it from the class path.
     */
    public void disable() {
        if (!this.isEnabled()) {
            throw new FusionException("Cannot disable the addon when it's not enabled");
        }

        this.onDisable();
        this.setEnabled(false);
    }

    /**
     * Sets the addon class loader
     *
     * @param loader {@link AddonClassLoader}
     */
    public void setLoader(@NotNull AddonClassLoader loader) {
        this.loader = loader;
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
    public void setEnabled(boolean enabled) {
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
     * Sets the name of the addon.
     *
     * @param name the name of the addon
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the addon.
     *
     * @return the name of the addon
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Retrieves the folder of the addon.
     *
     * @return the folder of the addon
     */
    public Path getFolder() {
        return this.folder;
    }
}