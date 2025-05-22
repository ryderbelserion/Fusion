package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class IAbstractConfigFile<A extends IAbstractConfigFile<A, C, L>, C, L> extends ICustomFile<A> {

    protected C configuration;
    protected final L loader;

    public IAbstractConfigFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @NotNull final L loader) {
        super(path, actions);

        this.loader = loader;
    }

    public abstract void loadConfig() throws ConfigurateException;

    public abstract void saveConfig() throws ConfigurateException;

    public abstract C getConfig();

    @Override
    public A load() {
        if (isDirectory()) {
            if (this.isVerbose) {
                this.logger.warning(String.format("Cannot load configuration, as %s is a directory.", getFileName()));
            }

            return (A) this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                loadConfig();
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to load configuration file: " + getFileName(), exception);
            }
        });

        return (A) this;
    }

    @Override
    public A save() {
        if (isDirectory()) {
            if (this.isVerbose) {
                this.logger.warning(String.format("Cannot save configuration, as %s is a directory.", getFileName()));
            }

            return (A) this;
        }

        if (this.configuration == null) {
            if (this.isVerbose) {
                this.logger.severe(String.format("Configuration is null, cannot save %s!", getFileName()));
            }

            return (A) this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                saveConfig();
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to save configuration file: " + getFileName(), exception);
            }
        });

        return (A) this;
    }

    public C getConfiguration() {
        return this.configuration;
    }
}