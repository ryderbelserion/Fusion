package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public abstract class ICustomFile<T extends ICustomFile<T>> {

    protected final FusionCore api = FusionCore.Provider.get();

    protected final Logger logger = this.api.getLogger();

    protected final boolean isVerbose = this.api.isVerbose();

    private final boolean isStatic;
    private final Path path;

    public ICustomFile(@NotNull final Path path, final boolean isStatic) {
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

    public ICustomFile<T> saveDirectory() {
        return save();
    }

    public ICustomFile<T> write(@NotNull final String content) {
        if (getType() != FileType.LOG) {
            throw new FusionException("This file is not a log file");
        }

        return this;
    }

    public String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return switch (getType()) {
            case YAML -> getConfigurationNode().node(path).getString(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public String getStringValue(@NotNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    public boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return switch (getType()) {
            case YAML -> getConfigurationNode().node(path).getBoolean(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    public double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return switch (getType()) {
            case YAML -> getConfigurationNode().node(path).getDouble(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    public long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return switch (getType()) {
            case YAML -> getConfigurationNode().node(path).getLong(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    public int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return switch (getType()) {
            case YAML -> getConfigurationNode().node(path).getInt(defaultValue);
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        };
    }

    public int getIntValue(@NotNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    public List<String> getStringList(@NotNull final Object... path) {
        return switch (getType()) {
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
                this.logger.warning(String.format("Successfully deleted %s", getFileName()));
            }
        } catch (final IOException exception) {
            this.logger.warning(String.format("Failed to delete %s: %s", getPath(), exception));
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

    public FileType getType() {
        return FileType.NONE;
    }

    public Path getPath() {
        return this.path;
    }
}