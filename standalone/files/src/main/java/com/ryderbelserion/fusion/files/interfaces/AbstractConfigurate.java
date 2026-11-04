package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.objects.FileData;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;

@NullMarked
public abstract class AbstractConfigurate<T, L, N extends ScopedConfigurationNode> extends AbstractCustomFile<T, L, N> {

    public AbstractConfigurate(final FileManager fileManager, final FileData fileData) {
        super(fileManager, fileData);
    }

    @Override
    public AbstractConfigurate<T, L, N> load() {
        super.load();

        return this;
    }

    public String getStringValueWithDefault(final String defaultValue, final Object... path) {
        return getConfig().node(path).getString(defaultValue);
    }

    public String getStringValue(final Object... path) {
        return getStringValueWithDefault("", path);
    }

    public boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
        return getConfig().node(path).getBoolean(defaultValue);
    }

    public boolean getBooleanValue(final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    public double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
        return getConfig().node(path).getDouble(defaultValue);
    }

    public double getDoubleValue(final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    public long getLongValueWithDefault(final long defaultValue, final Object... path) {
        return getConfig().node(path).getLong(defaultValue);
    }

    public long getLongValue(final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    public int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return getConfig().node(path).getInt(defaultValue);
    }

    public int getIntValue(final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    public List<String> getStringList(final List<String> defaultValue, final Object... path) {
        final ScopedConfigurationNode node = getConfig().node(path);

        try {
            final List<String> list = node.getList(String.class);

            if (list != null) return list;

            return defaultValue;
        } catch (final SerializationException exception) {
            return List.of();
        }
    }

    public List<String> getStringList(final Object... path) {
        return getStringList(List.of(), path);
    }
}