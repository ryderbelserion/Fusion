package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exception.FusionException;
import com.ryderbelserion.fusion.core.files.CustomFile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class JsonCustomFile extends CustomFile<JsonCustomFile> {

    private BasicConfigurationNode configurationNode;
    private final JacksonConfigurationLoader loader;

    public JsonCustomFile(final File file, final boolean isDynamic, @Nullable final UnaryOperator<ConfigurationOptions> defaultOptions) {
        super(file, isDynamic, ".json");

        final JacksonConfigurationLoader.Builder builder = JacksonConfigurationLoader.builder();

        if (defaultOptions != null) {
            builder.defaultOptions(defaultOptions);
        }

        this.loader = builder.indent(2).file(file).build();
    }

    public JsonCustomFile(final File file, final boolean isDynamic) {
        this(file, isDynamic, null);
    }

    @Override
    public final FileType getFileType() {
        return FileType.YAML;
    }

    @Override
    public final JsonCustomFile loadConfiguration() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        this.configurationNode = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to load configuration file: " + getFileName(), exception);
            }
        }).join();

        return this;
    }

    @Override
    public final JsonCustomFile saveConfiguration() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        if (this.configurationNode == null) {
            if (this.isVerbose) {
                this.logger.error("Configuration is null, cannot save {}!", getFileName());
            }

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.loader.save(this.configurationNode);
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to save configuration file: " + getFileName(), exception);
            }
        });

        return this;
    }

    @Override
    public final String getStringValueWithDefault(final String defaultValue, final Object... path) {
        return this.configurationNode.node(path).getString(defaultValue);
    }

    @Override
    public final String getStringValue(final Object... path) {
        return getStringValueWithDefault("", path);
    }

    @Override
    public final boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
        return this.configurationNode.node(path).getBoolean(defaultValue);
    }

    @Override
    public final boolean getBooleanValue(final Object... path) {
        return getBooleanValueWithDefault(Boolean.FALSE, path);
    }

    @Override
    public final double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
        return this.configurationNode.node(path).getDouble(defaultValue);
    }

    @Override
    public final double getDoubleValue(final Object... path) {
        return getDoubleValueWithDefault(Double.NaN, path);
    }

    @Override
    public final long getLongValueWithDefault(final long defaultValue, final Object... path) {
        return this.configurationNode.node(path).getLong(defaultValue);
    }

    @Override
    public final long getLongValue(final Object... path) {
        return getLongValueWithDefault(Long.MIN_VALUE, path);
    }

    @Override
    public final int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return this.configurationNode.node(path).getInt(defaultValue);
    }

    @Override
    public final int getIntValue(final Object... path) {
        return getIntValueWithDefault(Integer.MIN_VALUE, path);
    }

    @Override
    public final List<String> getStringList(final Object... path) {
        try {
            return this.configurationNode.node(path).getList(String.class);
        } catch (SerializationException exception) {
            throw new FusionException("Failed to serialize " + Arrays.toString(path), exception);
        }
    }

    @Override
    public final BasicConfigurationNode getBasicConfigurationNode() {
        return this.configurationNode;
    }

    @Override
    public final CustomFile<JsonCustomFile> getInstance() {
        return this;
    }

    @Override
    public final boolean isConfigurationLoaded() {
        return this.configurationNode != null;
    }
}