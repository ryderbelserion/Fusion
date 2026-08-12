package com.ryderbelserion.fusion.core.files.types.configurate;

import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.enums.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.IConfigurate;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
public final class JsonCustomFile extends ICustomFile<JsonCustomFile, BasicConfigurationNode, GsonConfigurationLoader> implements IConfigurate {

    public JsonCustomFile(final String jarFolder, final Path path, final Consumer<JsonCustomFile> consumer) {
        super(jarFolder, path);

        consumer.accept(this);

        this.loader = GsonConfigurationLoader.builder()
                .indent(this.indent)
                .lenient(this.isLenient)
                .headerMode(this.headerMode)
                .path(getPath()).defaultOptions(getOptions())
                .build();
    }

    public JsonCustomFile(final Path path, final Consumer<JsonCustomFile> consumer) {
        this("", path, consumer);
    }

    @Override
    public BasicConfigurationNode loadConfig() throws IOException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws IOException {
        this.loader.save(this.configuration);
    }

    @Override
    public FileType getFileType() {
        return FileType.JSON;
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
        final BasicConfigurationNode node = getConfiguration().node(path);

        try {
            final List<String> list = node.getList(String.class);

            if (list != null) return list;

            return defaultValue;
        } catch (final SerializationException exception) {
            throw new FusionException("Failed to serialize %s!".formatted(node.path()), exception);
        }
    }
}