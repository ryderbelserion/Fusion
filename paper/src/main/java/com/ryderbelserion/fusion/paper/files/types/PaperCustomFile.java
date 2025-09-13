package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.files.IConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class PaperCustomFile extends IConfigFile<PaperCustomFile, YamlConfiguration, YamlConfiguration> {

    public PaperCustomFile(@NotNull final Path path, final List<FileAction> actions) {
        super(path, actions, new YamlConfiguration());
    }

    @Override
    public @NotNull final YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(getPath().toFile());
    }

    @Override
    public void saveConfig() throws IOException {
        this.configuration.save(getPath().toFile());
    }

    @Override
    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return getConfiguration().getBoolean(Arrays.toString(path), defaultValue);
    }

    @Override
    public boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    @Override
    public double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return getConfiguration().getDouble(Arrays.toString(path), defaultValue);
    }

    @Override
    public double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    @Override
    public int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return getConfiguration().getInt(Arrays.toString(path), defaultValue);
    }

    @Override
    public int getIntValue(@NotNull Object... path) {
        return getIntValueWithDefault(0, path);
    }

    @Override
    public @NotNull List<String> getStringList(@NotNull final List<String> defaultValue, @NotNull final Object... path) {
        final YamlConfiguration configuration = getConfiguration();

        final List<String> list = configuration.getStringList(Arrays.toString(path));

        return list.isEmpty() ? defaultValue : list;
    }

    @Override
    public @NotNull List<String> getStringList(@NotNull final Object... path) {
        return getStringList(List.of(), path);
    }

    @Override
    public long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return getConfiguration().getLong(Arrays.toString(path), defaultValue);
    }

    @Override
    public boolean isLoaded() {
        return this.configuration != null;
    }

    @Override
    public long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    @Override
    public @NotNull FileType getFileType() {
        return FileType.PAPER;
    }
}