package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public final class JsonCustomFile extends ICustomFile<JsonCustomFile, BasicConfigurationNode, GsonConfigurationLoader, ConfigurationOptions> {

    public JsonCustomFile(@NotNull final FileManager fileManager, @NotNull final Path path, @NotNull final Consumer<JsonCustomFile> consumer) {
        super(fileManager, path);

        this.options = ConfigurationOptions.defaults();

        consumer.accept(this);

        this.loader = GsonConfigurationLoader.builder().path(getPath()).defaultOptions(getOptions()).build();
    }

    @Override
    public @NotNull BasicConfigurationNode loadConfig() throws IOException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws IOException {
        this.loader.save(this.configuration);
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
        final BasicConfigurationNode node = getConfiguration().node(path);

        try {
            final List<String> list = node.getList(String.class);

            if (list != null) return list;

            return defaultValue;
        } catch (final SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    @Override
    public boolean isLoaded() {
        return this.configuration != null;
    }

    @Override
    public @NotNull FileType getFileType() {
        return FileType.JSON;
    }
}