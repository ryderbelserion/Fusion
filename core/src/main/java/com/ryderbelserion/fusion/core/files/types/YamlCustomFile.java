package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class YamlCustomFile extends ICustomFile<YamlCustomFile, CommentedConfigurationNode, YamlConfigurationLoader, ConfigurationOptions> {

    public YamlCustomFile(@NotNull final FileManager fileManager, @NotNull final Consumer<YamlCustomFile> consumer) {
        super(fileManager);

        this.options = ConfigurationOptions.defaults();

        consumer.accept(this);

        this.loader = YamlConfigurationLoader.builder().path(getPath()).defaultOptions(getOptions()).build();
    }

    @Override
    public @NotNull CommentedConfigurationNode loadConfig() throws IOException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws IOException {
        this.loader.save(this.configuration);
    }

    /**
     * Retrieves a string value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the string value or the default if missing
     */
    @Override
    public @NotNull final String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getString(defaultValue);
    }

    /**
     * Retrieves a boolean value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the boolean value or the default if missing
     */
    @Override
    public final boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getBoolean(defaultValue);
    }

    /**
     * Retrieves a double value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the double value or the default if missing
     */
    @Override
    public final double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getDouble(defaultValue);
    }

    /**
     * Retrieves a long value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the long value or the default if missing
     */
    @Override
    public final long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getLong(defaultValue);
    }

    /**
     * Retrieves an integer value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the integer value or the default if missing
     */
    @Override
    public final int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getInt(defaultValue);
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

            if (list != null) return list;

            return defaultValue;
        } catch (final SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    @Override
    public @NotNull final FileType getFileType() {
        return FileType.YAML;
    }
}