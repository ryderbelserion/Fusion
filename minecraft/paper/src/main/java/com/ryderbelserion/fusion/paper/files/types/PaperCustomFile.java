package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.IConfigurate;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
public class PaperCustomFile extends ICustomFile<PaperCustomFile, YamlConfiguration, Object> implements IConfigurate {

    private final File file;

    public PaperCustomFile(final PaperFileManager fileManager, final Path path, final Consumer<PaperCustomFile> consumer) {
        super(fileManager, path);

        this.file = getPath().toFile();

        consumer.accept(this);
    }

    @Override
    public YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public final FileType getFileType() {
        return FileType.PAPER_YAML;
    }

    @Override
    public void saveConfig() throws IOException {
        this.configuration.save(this.file);
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
    public List<String> getStringList(final List<String> defaultValue, final Object... path) {
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
    public boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
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
    public double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
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
    public long getLongValueWithDefault(final long defaultValue, final Object... path) {
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
    public int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return getConfiguration().getInt(Arrays.toString(path), defaultValue);
    }
}