package com.ryderbelserion.fusion.addons.interfaces;

import com.ryderbelserion.fusion.addons.AddonClassLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * The addon class, handles all things related to addons.
 */
public abstract class IAddon {

    /**
     * The addon class, handles all things related to addons.
     */
    public IAddon() {

    }

    private AddonClassLoader loader;
    private boolean isEnabled;
    private Logger logger;
    private String name;
    private Path folder;
    private String group;

    /**
     * Enables the addon.
     */
    public void onEnable() {
        setEnabled(true);
    }

    /**
     * Disables the addon.
     */
    public void onDisable() {
        setEnabled(false);
    }

    public void onReload() {}

    /**
     * Enables an addon, this includes adding it to the class path.
     *
     * @param folder the addon's folder
     */
    public void enable(@NotNull final Path folder) {
        if (this.isEnabled()) {
            throw new IllegalStateException("Cannot enable the addon when it's already enabled");
        }

        this.folder = folder;

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
     * Sets the addon class loader
     *
     * @param loader {@link AddonClassLoader}
     */
    public void setLoader(@NotNull final AddonClassLoader loader) {
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
     * Sets the name of the addon, and creates a logger impl.
     *
     * @param name the name of the addon
     */
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Gets the group i.e. the domain
     *
     * @param group the group
     */
    public void setGroup(@NotNull final String group) {
        this.logger = LoggerFactory.getLogger(group);
        this.group = group;
    }

    /**
     * Gets an instance of the logger.
     *
     * @return the logger instance of this addon
     */
    public @NotNull Logger getLogger() {
        return this.logger;
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
     * Retrieves the group path.
     *
     * @return the group path of the addon
     */
    public @NotNull String getGroup() {
        return this.group;
    }

    /**
     * Retrieves the folder of the addon.
     *
     * @return the folder of the addon
     */
    public @NotNull Path getFolder() {
        return this.folder;
    }
}