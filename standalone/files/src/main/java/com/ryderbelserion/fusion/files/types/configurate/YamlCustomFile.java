package com.ryderbelserion.fusion.files.types.configurate;

import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.IConfigurate;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
public final class YamlCustomFile extends ICustomFile<YamlCustomFile, CommentedConfigurationNode, YamlConfigurationLoader> implements IConfigurate {

    public YamlCustomFile(final FileManager fileManager, final String jarFolder, final Path path, final Consumer<YamlCustomFile> consumer) {
        super(fileManager, jarFolder, path);

        consumer.accept(this);

        this.loader = YamlConfigurationLoader.builder()
                .indent(this.indent)
                .nodeStyle(this.nodeStyle)
                .headerMode(this.headerMode)
                .commentsEnabled(this.hasComments)
                .path(getPath())
                .defaultOptions(getOptions())
                .build();
    }

    public YamlCustomFile(final FileManager fileManager, final Path path, final Consumer<YamlCustomFile> consumer) {
        this(fileManager, "", path, consumer);
    }

    @Override
    public CommentedConfigurationNode loadConfig() throws IOException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws IOException {
        this.loader.save(this.configuration);
    }

    @Override
    public FileType getFileType() {
        return FileType.YAML;
    }

    @SuppressWarnings("ConstantValue")
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
    public String getStringValueWithDefault(final String defaultValue, final Object... path) {
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
    public boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
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
    public double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
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
    public long getLongValueWithDefault(final long defaultValue, final Object... path) {
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
    public int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return getConfiguration().node(path).getInt(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getStringList(final List<String> defaultValue, final Object... path) {
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