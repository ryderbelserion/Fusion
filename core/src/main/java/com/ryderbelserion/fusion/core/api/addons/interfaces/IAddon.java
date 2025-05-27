package com.ryderbelserion.fusion.core.api.addons.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.addons.AddonClassLoader;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public abstract class IAddon {

    protected final FusionCore fusion = FusionCore.Provider.get();

    private AddonClassLoader loader;
    private boolean isEnabled;
    private String name;
    private Path folder;

    public abstract void onEnable();

    public abstract void onDisable();

    public ILogger getLogger() {
        return this.fusion.getLogger();
    }

    public void enable(@NotNull final Path folder) {
        if (this.isEnabled()) {
            throw new FusionException("Cannot enable the addon when it's already enabled");
        }

        this.folder = folder;

        this.onEnable();
        this.setEnabled(true);
    }

    public void disable() {
        if (!this.isEnabled()) {
            throw new FusionException("Cannot disable the addon when it's not enabled");
        }

        this.onDisable();
        this.setEnabled(false);
    }

    public void setLoader(@NotNull final AddonClassLoader loader) {
        this.loader = loader;
    }

    public AddonClassLoader getLoader() {
        return this.loader;
    }

    public void setEnabled(final boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Path getFolder() {
        return this.folder;
    }
}