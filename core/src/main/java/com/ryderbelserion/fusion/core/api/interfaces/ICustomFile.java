package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.objects.FileKey;
import com.ryderbelserion.fusion.core.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public abstract class ICustomFile<T extends ICustomFile<T>> {

    protected final FusionCore api = FusionCore.Provider.get();

    protected final ComponentLogger logger = this.api.getLogger();

    protected final boolean isVerbose = this.api.isVerbose();

    private final boolean isStatic;
    private final Path path;

    public ICustomFile(@NotNull final Path path, boolean isStatic) {
        this.isStatic = isStatic;
        this.path = path;
    }

    public ICustomFile(@NotNull final Path path) {
        this(path, false);
    }

    public ICustomFile<T> build() {
        return this;
    }

    public abstract ICustomFile<T> load();

    public abstract ICustomFile<T> save();

    public String getStringValueWithDefault(final String defaultValue, final Object... path) {
        return switch (getType()) {
            case JSON -> getBasicConfigurationNode().node(path).getString(defaultValue);
            case YAML -> getConfigurationNode().node(path).getString(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public String getStringValue(final Object... path) {
        return getStringValueWithDefault("", path);
    }

    public boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
        return switch (getType()) {
            case JSON -> getBasicConfigurationNode().node(path).getBoolean(defaultValue);
            case YAML -> getConfigurationNode().node(path).getBoolean(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public boolean getBooleanValue(final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    public double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
        return switch (getType()) {
            case JSON -> getBasicConfigurationNode().node(path).getDouble(defaultValue);
            case YAML -> getConfigurationNode().node(path).getDouble(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public double getDoubleValue(final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    public long getLongValueWithDefault(final long defaultValue, final Object... path) {
        return switch (getType()) {
            case JSON -> getBasicConfigurationNode().node(path).getLong(defaultValue);
            case YAML -> getConfigurationNode().node(path).getLong(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public long getLongValue(final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    public int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return switch (getType()) {
            case JSON -> getBasicConfigurationNode().node(path).getInt(defaultValue);
            case YAML -> getConfigurationNode().node(path).getInt(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public int getIntValue(final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    public List<String> getStringList(final Object... path) {
        return switch (getType()) {
            case JSON -> {
                try {
                    yield getBasicConfigurationNode().node(path).getList(String.class);
                } catch (SerializationException exception) {
                    throw new FusionException("Failed to serialize " + Arrays.toString(path), exception);
                }
            }

            case YAML -> {
                try {
                    yield getConfigurationNode().node(path).getList(String.class);
                } catch (SerializationException exception) {
                    throw new FusionException("Failed to serialize " + Arrays.toString(path), exception);
                }
            }

            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public CommentedConfigurationNode getConfigurationNode() {
        return null;
    }

    public BasicConfigurationNode getBasicConfigurationNode() {
        return null;
    }

    public ICustomFile<T> delete() {
        try {
            Files.deleteIfExists(getPath());

            if (this.isVerbose) {
                this.logger.warn("Successfully deleted {}", getFileName());
            }
        } catch (final IOException exception) {
            this.logger.warn("Failed to delete {}: {}", getPath(), exception);
        }

        return this;
    }

    public ICustomFile<T> getInstance() {
        return this;
    }

    public boolean isDirectory() {
        return Files.isDirectory(this.path);
    }

    public String getFileName() {
        return this.path.getFileName().toString();
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public boolean isLoaded() {
        return Files.exists(this.path);
    }

    public FileKey getType() {
        return null;
    }

    public Path getPath() {
        return this.path;
    }
}