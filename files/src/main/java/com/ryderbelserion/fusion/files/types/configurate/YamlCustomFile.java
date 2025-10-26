package com.ryderbelserion.fusion.files.types.configurate;

import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.IConfigurate;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public final class YamlCustomFile extends ICustomFile<YamlCustomFile, CommentedConfigurationNode, YamlConfigurationLoader, ConfigurationOptions> implements IConfigurate {

    public YamlCustomFile(@NotNull final FileManager fileManager, @NotNull final Path path, @NotNull final Consumer<YamlCustomFile> consumer) {
        super(fileManager, path);

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

    @Override
    public @NotNull FileType getFileType() {
        return FileType.YAML;
    }

    @Override
    public boolean isLoaded() {
        return this.configuration != null;
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultValue {@inheritDoc}
     * @param path         {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @NotNull String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getString(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultValue {@inheritDoc}
     * @param path         {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getBoolean(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultValue {@inheritDoc}
     * @param path         {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getDouble(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultValue {@inheritDoc}
     * @param path         {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getLong(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultValue {@inheritDoc}
     * @param path         {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getInt(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @NotNull List<String> getStringList(@NotNull final List<String> defaultValue, @NotNull final Object... path) {
        final CommentedConfigurationNode node = getConfiguration().node(path);

        try {
            final List<String> list = node.getList(String.class);

            if (list != null) return list;

            return defaultValue;
        } catch (final SerializationException exception) {
            throw new FileException("Failed to serialize %s!".formatted(node.path()), exception);
        }
    }
}