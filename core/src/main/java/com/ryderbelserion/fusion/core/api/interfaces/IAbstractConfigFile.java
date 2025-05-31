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

    public abstract C loadConfig() throws ConfigurateException;

    public abstract void saveConfig() throws ConfigurateException;

    public @NotNull String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return "";
    }

    public @NotNull String getStringValue(@NotNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return false;
    }

    public boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    public double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return -1.0;
    }

    public double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    public long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return -1L;
    }

    public long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    public int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return -1;
    }

    public int getIntValue(@NotNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    public @NotNull List<String> getStringList(@NotNull final Object... path) {
        return List.of();
    }

    @Override
    public @NotNull A load() {
        if (isDirectory()) {
            this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());

            return (A) this;
        }

        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return loadConfig();
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to load configuration file: " + getFileName(), exception);
            }
        }).join();

        return (A) this;
    }

    @Override
    public @NotNull A save() {
        if (isDirectory()) {
            this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());

            return (A) this;
        }

        if (this.configuration == null) {
            this.logger.error("Configuration is null, cannot save {}!", getFileName());

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

    public @NotNull C getConfiguration() {
        return this.configuration;
    }
}