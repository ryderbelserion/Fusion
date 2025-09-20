package com.ryderbelserion.fusion.core.files.interfaces;

import com.ryderbelserion.fusion.core.files.enums.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// I i.e. JaluCustomFile, or YamlCustomFile.
// C i.e. the SettingsManager, or CommentedConfigurationNode/BasicConfigurationNode.
// L i.e. the SettingsManagerBuilder, or YamlConfigurationLoader.
// O i.e. options that configure the functionality of L.
public abstract class ICustomFile<I, C, L, O> {

    protected final List<FileAction> actions = new ArrayList<>();
    protected final FileManager fileManager;

    protected C configuration;
    protected L loader;
    protected O options = null;

    protected FileType fileType;
    protected Path path;

    public ICustomFile(@NotNull final FileManager fileManager, @NotNull final Path path) {
        this.fileManager = fileManager;
        this.path = path;
    }

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

    public void saveConfig(@NotNull final String content) throws IOException {
        saveConfig();
    }

    public void saveConfig() throws IOException {

    }

    public abstract @NotNull C loadConfig() throws IOException;

    public @NotNull C getConfiguration() {
        return this.configuration;
    }

    public @NotNull I load() {
        if (isDirectory()) return (I) this;

        final Path path = getPath();

        if (hasAction(FileAction.EXTRACT_FILE)) this.fileManager.extractFile(path);

        if (hasAction(FileAction.EXTRACT_FOLDER)) this.fileManager.extractFolder(path.getFileName().toString(), path.getParent());

        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return loadConfig();
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        }).join();

        return (I) this;
    }

    public @NotNull I save(@NotNull final String content) {
        if (isDirectory()) return (I) this;

        if (this.configuration == null) return (I) this;

        CompletableFuture.runAsync(() -> {
            try {
                saveConfig(content);
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        });

        return (I) this;
    }

    public @NotNull I save() {
        return save("");
    }

    public boolean isLoaded() {
        return Files.exists(this.path);
    }

    public void setOptions(@NotNull final Consumer<O> options) {
        options.accept(this.options);
    }

    public void removeAction(@NotNull final FileAction action) {
        this.actions.remove(action);
    }

    public boolean hasAction(@NotNull final FileAction action) {
        return this.actions.contains(action);
    }

    public void addAction(@NotNull final FileAction action) {
        this.actions.add(action);
    }

    public @NotNull O getOptions() {
        return this.options;
    }

    public @NotNull String getFileName() {
        return this.path.getFileName().toString();
    }

    public @NotNull String getPrettyName() {
        return getFileName().replace(getFileType().getExtension(), "");
    }

    public @NotNull Path getPath() {
        return this.path;
    }

    public boolean isDirectory() {
        return Files.isDirectory(getPath());
    }

    public @NotNull FileType getFileType() {
        return this.fileType;
    }

    public void setLoader(@NotNull final L loader) {
        this.loader = loader;
    }
}