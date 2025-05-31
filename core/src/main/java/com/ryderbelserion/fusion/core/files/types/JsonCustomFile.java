package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.IAbstractConfigFile;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.function.UnaryOperator;

public class JsonCustomFile extends IAbstractConfigFile<JsonCustomFile, BasicConfigurationNode, JacksonConfigurationLoader> {

    public JsonCustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        super(path, actions, JacksonConfigurationLoader.builder().defaultOptions(options != null ? options : configurationOptions -> configurationOptions).indent(2).path(path).build());
    }

    @Override
    public BasicConfigurationNode loadConfig() throws ConfigurateException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws ConfigurateException {
        this.loader.save(this.configuration);
    }

    public @NotNull String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getString(defaultValue);
    }

    public @NotNull String getStringValue(@NotNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getBoolean(defaultValue);
    }

    public boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    public double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getDouble(defaultValue);
    }

    public double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    public long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return getConfiguration().node(path).getLong(defaultValue);
    }

    public long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    public int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return getConfiguration().node(path).getInt(defaultValue);
    }

    public int getIntValue(@NotNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    @Override
    public @NotNull final FileType getFileType() {
        return FileType.JSON;
    }
}