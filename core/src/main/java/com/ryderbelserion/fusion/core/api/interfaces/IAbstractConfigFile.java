package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
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
public abstract class IAbstractConfigFile<A extends IAbstractConfigFile<A, C, L>, C, L> extends ICustomFile<A> {

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
    public IAbstractConfigFile(@NotNull Path path, @NotNull List<FileAction> actions, @NotNull L loader) {
        super(path, actions);

        this.loader = loader;
    }

    /**
     * Loads the configuration.
     *
     * <p>This method retrieves the configuration file and initializes its contents.</p>
     *
     * @return the loaded configuration instance
     * @throws ConfigurateException if an error occurs during the loading process
     */
    public abstract @NotNull C loadConfig() throws ConfigurateException;

    /**
     * Saves the current configuration.
     *
     * <p>This method writes changes back to the configuration file.</p>
     *
     * @throws ConfigurateException if an error occurs while saving
     */
    public abstract void saveConfig() throws ConfigurateException;

    /**
     * Retrieves a string value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the string value or the default if missing
     */
    public @NotNull String getStringValueWithDefault(@NotNull String defaultValue, @NotNull Object... path) {
        return "";
    }

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the string value or an empty string if missing
     */
    public @NotNull String getStringValue(@NotNull Object... path) {
        return getStringValueWithDefault("", path);
    }

    /**
     * Retrieves a boolean value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the boolean value or the default if missing
     */
    public boolean getBooleanValueWithDefault(boolean defaultValue, @NotNull Object... path) {
        return false;
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the boolean value or false if missing
     */
    public boolean getBooleanValue(@NotNull Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    /**
     * Retrieves a double value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the double value or the default if missing
     */
    public double getDoubleValueWithDefault(double defaultValue, @NotNull Object... path) {
        return -1.0;
    }

    /**
     * Retrieves a double value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the double value or 0.0 if missing
     */
    public double getDoubleValue(@NotNull Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    /**
     * Retrieves a long value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the long value or the default if missing
     */
    public long getLongValueWithDefault(long defaultValue, @NotNull Object... path) {
        return -1L;
    }

    /**
     * Retrieves a long value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the long value or 0L if missing
     */
    public long getLongValue(@NotNull Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    /**
     * Retrieves an integer value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the integer value or the default if missing
     */
    public int getIntValueWithDefault(int defaultValue, @NotNull Object... path) {
        return -1;
    }

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the integer value or 0 if missing
     */
    public int getIntValue(@NotNull Object... path) {
        return getIntValueWithDefault(0, path);
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    public @NotNull List<String> getStringList(@NotNull Object... path) {
        return List.of();
    }

    /**
     * Loads the configuration, This is a generic way to support multiple configuration types.
     *
     * @return {@link A}
     */
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

    /**
     * Saves the configuration as is.
     *
     * @return {@link A}
     */
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