package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.IConfigurate;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PaperCustomFile extends ICustomFile<PaperCustomFile, YamlConfiguration, Object, Object> implements IConfigurate {

    private final File file;

    public PaperCustomFile(@NotNull final FileManager fileManager, @NotNull final Path path, @NotNull final Consumer<PaperCustomFile> consumer) {
        super(fileManager, fileManager.getSource(), path);

        this.file = getPath().toFile();

        consumer.accept(this);
    }

    @Override
    public @NotNull YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public @NotNull final FileType getFileType() {
        return FileType.PAPER_YAML;
    }

    @Override
    public void saveConfig() throws IOException {
        this.configuration.save(this.file);
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
    public @NotNull List<String> getStringList(@NotNull final List<String> defaultValue, @NotNull final Object... path) {
        final YamlConfiguration configuration = getConfiguration();

        final List<String> list = configuration.getStringList(Arrays.toString(path));

        return list.isEmpty() ? defaultValue : list;
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
        return getConfiguration().getBoolean(Arrays.toString(path), defaultValue);
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
        return getConfiguration().getDouble(Arrays.toString(path), defaultValue);
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
        return getConfiguration().getLong(Arrays.toString(path), defaultValue);
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
        return getConfiguration().getInt(Arrays.toString(path), defaultValue);
    }
}