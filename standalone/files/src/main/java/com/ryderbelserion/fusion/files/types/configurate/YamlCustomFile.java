package com.ryderbelserion.fusion.files.types.configurate;

import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.IConfigurate;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public final class YamlCustomFile extends ICustomFile<YamlCustomFile, CommentedConfigurationNode, YamlConfigurationLoader> implements IConfigurate {

    public YamlCustomFile(@NonNull final FileManager fileManager, @NonNull final String jarFolder, @NonNull final Path path, @NonNull final Consumer<YamlCustomFile> consumer) {
        super(fileManager, jarFolder, path);

        consumer.accept(this);

        this.loader = YamlConfigurationLoader.builder()
                .indent(this.indent)
                .nodeStyle(this.nodeStyle)
                .headerMode(this.headerMode).path(getPath()).defaultOptions(getOptions()).build();
    }

    public YamlCustomFile(@NonNull final FileManager fileManager, @NonNull final Path path, @NonNull final Consumer<YamlCustomFile> consumer) {
        this(fileManager, "", path, consumer);
    }

    @Override
    public @NonNull CommentedConfigurationNode loadConfig() throws IOException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws IOException {
        this.loader.save(this.configuration);
    }

    @Override
    public @NonNull FileType getFileType() {
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
    public @NonNull String getStringValueWithDefault(@NonNull final String defaultValue, @NonNull final Object... path) {
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
    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NonNull final Object... path) {
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
    public double getDoubleValueWithDefault(final double defaultValue, @NonNull final Object... path) {
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
    public long getLongValueWithDefault(final long defaultValue, @NonNull final Object... path) {
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
    public int getIntValueWithDefault(final int defaultValue, @NonNull final Object... path) {
        return getConfiguration().node(path).getInt(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @NonNull List<String> getStringList(@NonNull final List<String> defaultValue, @NonNull final Object... path) {
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