package com.ryderbelserion.fusion.core.files;

import com.ryderbelserion.fusion.core.FusionLayout;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exception.FusionException;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class CustomFile<T extends CustomFile<T>> {

    protected final FusionLayout api = FusionProvider.get();

    protected final ComponentLogger logger = this.api.getLogger();

    protected final boolean isVerbose = this.api.isVerbose();

    private final boolean isDynamic;
    private final File file;

    public CustomFile(@NotNull final File file, boolean isDynamic) {
        this.isDynamic = isDynamic;
        this.file = file;
    }

    public CustomFile(@NotNull final File file) {
        this.isDynamic = false;
        this.file = file;
    }

    public CustomFile<T> build() {
        return this;
    }

    public abstract CustomFile<T> load();

    public abstract CustomFile<T> save();

    public CustomFile<T> write(@NotNull final String content) {
        if (getType() != FileType.LOG) {
            throw new FusionException("This file is not a log file");
        }

        return this;
    }

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

    public CustomFile<T> delete() {
        final File file = getFile();

        if (file != null && file.exists() && file.delete()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully deleted {}", getFileName());
            }
        }

        return this;
    }

    public CustomFile<T> getInstance() {
        return this;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public FileType getType() {
        return FileType.NONE;
    }

    public boolean isLoaded() {
        return this.file.exists();
    }

    public File getFile() {
        return this.file;
    }
}