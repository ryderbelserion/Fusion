package com.ryderbelserion.fusion.core.api.interfaces.files;

import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an abstract configuration file that extends {@link ICustomFile}.
 *
 * <p>This class provides a base structure for configuration file handling, including
 * a configuration instance and a loader. It employs generics for flexibility in defining
 * specific configurations and loaders.</p>
 *
 * @param <A> the type of the abstract config file, enabling self-referential generics
 * @param <C> the type representing the configuration instance
 * @param <L> the type representing the configuration loader
 */
public abstract class IConfigFile<A extends IConfigFile<A, C, L>, C, L> extends ICustomFile<A> {

    /**
     * The configuration instance associated with this file.
     */
    protected C configuration;

    /**
     * The loader responsible for handling configuration operations.
     */
    protected final L loader;

    /**
     * Constructs an {@code IAbstractConfigFile} with the specified file path, actions, and loader.
     *
     * @param path    the file path associated with the configuration file
     * @param actions the list of file actions applied to the configuration file
     * @param loader  the loader responsible for configuration management
     */
    protected IConfigFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @NotNull final L loader) {
        super(path, actions);

        this.loader = loader;
    }

    /**
     * Loads the configuration.
     *
     * <p>This method retrieves the configuration file and initializes its contents.</p>
     *
     * @return the loaded configuration instance
     * @throws IOException if an error occurs during the loading process
     */
    public abstract @NotNull C loadConfig() throws IOException;

    /**
     * Saves the current configuration.
     *
     * <p>This method writes changes back to the configuration file.</p>
     *
     * @throws IOException if an error occurs while saving
     */
    public abstract void saveConfig() throws IOException;

    /**
     * Retrieves a string value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the string value or the default if missing
     */
    public @NotNull String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return "";
    }

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the string value or an empty string if missing
     */
    public @NotNull String getStringValue(@NotNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    /**
     * Retrieves a boolean value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the boolean value or the default if missing
     */
    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return false;
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the boolean value or false if missing
     */
    public boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    /**
     * Retrieves a double value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the double value or the default if missing
     */
    public double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return -1.0;
    }

    /**
     * Retrieves a double value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the double value or 0.0 if missing
     */
    public double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    /**
     * Retrieves a long value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the long value or the default if missing
     */
    public long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return -1L;
    }

    /**
     * Retrieves a long value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the long value or 0L if missing
     */
    public long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    /**
     * Retrieves an integer value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the integer value or the default if missing
     */
    public int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return -1;
    }

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the integer value or 0 if missing
     */
    public int getIntValue(@NotNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    public @NotNull List<String> getStringList(@NotNull final List<String> defaultValue, @NotNull final Object... path) {
        return List.of();
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    public @NotNull List<String> getStringList(@NotNull final Object... path) {
        return getStringList(List.of(), path);
    }

    /**
     * Loads the configuration, This is a generic way to support multiple configuration types.
     *
     * @return {@link A}
     */
    @Override
    public @NotNull A load() {
        if (isDirectory()) {
            this.fusion.log("warn", "Cannot load configuration, as {} is a directory.", getFileName());

            return (A) this;
        }

        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return loadConfig();
            } catch (final IOException exception) {
                throw new FusionException(String.format("Failed to load configuration file %s!", getFileName()), exception);
            }
        }).join();

        return (A) this;
    }

    /**
     * Saves the configuration as is.
     *
     * @return {@link A}
     */
    @Override
    public @NotNull A save() {
        if (isDirectory()) {
            this.fusion.log("warn", "Cannot save configuration, as {} is a directory.", getFileName());

            return (A) this;
        }

        if (this.configuration == null) {
            this.fusion.log("error", "Configuration is null, cannot save {}!", getFileName());

            return (A) this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                saveConfig();
            } catch (final IOException exception) {
                throw new FusionException(String.format("Failed to save configuration file %s!", getFileName()), exception);
            }
        }).thenAccept(v -> this.fusion.log("info", "Successfully saved {}!", getFileName()));

        return (A) this;
    }

    public @NotNull C getConfiguration() {
        return this.configuration;
    }
}