package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.objects.FileData;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;

@NullMarked
public abstract class AbstractConfigurate<L, N extends ScopedConfigurationNode> extends AbstractCustomFile<L, N> {

    public AbstractConfigurate(final FileManager fileManager, final FileData fileData) {
        super(fileManager, fileData);
    }

    public abstract <V> void write(final V value, final Object... args);

    public abstract <V> V read(final V value, final Object... args);

    public List<String> getStringList(final List<String> defaultValue, final Object... path) {
        final ScopedConfigurationNode node = getConfig().node(path);

        try {
            final List<String> list = node.getList(String.class);

            if (list != null) {
                return list;
            }

            return defaultValue;
        } catch (final SerializationException exception) {
            return List.of();
        }
    }

    public List<String> getStringList(final Object... path) {
        return getStringList(List.of(), path);
    }
}