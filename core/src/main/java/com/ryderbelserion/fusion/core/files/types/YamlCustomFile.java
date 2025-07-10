package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.files.IConfigFile;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Loads, reloads, or saves existing files built using Configurate Yaml.
 */
public class YamlCustomFile extends IConfigFile<YamlCustomFile, CommentedConfigurationNode, YamlConfigurationLoader> {

    /**
     * Constructs a {@code YamlCustomFile} with the specified file path, actions, and loader.
     *
     * @param path    the file path associated with the configuration file
     * @param actions the list of file actions applied to the configuration file
     * @param options the options responsible for configuring the formatting
     */
    public YamlCustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        super(path, actions, YamlConfigurationLoader.builder().defaultOptions(options != null ? options : configurationOptions -> configurationOptions).indent(2).path(path).build());
    }

    /**
     * Loads the configuration.
     *
     * <p>This method retrieves the configuration file and initializes its contents.</p>
     *
     * @return the loaded configuration instance
     * @throws ConfigurateException if an error occurs during the loading process
     */
    @Override
    public @NotNull final CommentedConfigurationNode loadConfig() throws ConfigurateException {
        return this.loader.load();
    }

    /**
     * Saves the current configuration.
     *
     * <p>This method writes changes back to the configuration file.</p>
     *
     * @throws ConfigurateException if an error occurs while saving
     */
    @Override
    public final void saveConfig() throws ConfigurateException {
        this.loader.save(this.configuration);
    }

    /**
     * Retrieves a string value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the string value or the default if missing
     */
    public @NotNull final String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getString(defaultValue);
    }

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the string value or an empty string if missing
     */
    public @NotNull final String getStringValue(@NotNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    /**
     * Retrieves a boolean value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the boolean value or the default if missing
     */
    public final boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getBoolean(defaultValue);
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the boolean value or false if missing
     */
    public final boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    /**
     * Retrieves a double value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the double value or the default if missing
     */
    public final double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getDouble(defaultValue);
    }

    /**
     * Retrieves a double value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the double value or 0.0 if missing
     */
    public final double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    /**
     * Retrieves a long value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the long value or the default if missing
     */
    public final long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getLong(defaultValue);
    }

    /**
     * Retrieves a long value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the long value or 0L if missing
     */
    public final long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    /**
     * Retrieves an integer value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the integer value or the default if missing
     */
    public final int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getInt(defaultValue);
    }

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the integer value or 0 if missing
     */
    public final int getIntValue(@NotNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    @Override
    public @NotNull final List<String> getStringList(@NotNull final List<String> defaultValue, @NotNull final Object... path) {
        final CommentedConfigurationNode node = getConfiguration().node(path);

        try {
            final List<String> list = node.getList(String.class);

            if (list != null) {
                return list;
            }

            return defaultValue;
        } catch (final SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    /**
     * Retrieves the file type.
     *
     * @return the {@link FileType}
     */
    @Override
    public @NotNull final FileType getFileType() {
        return FileType.CONFIGURATE;
    }
}