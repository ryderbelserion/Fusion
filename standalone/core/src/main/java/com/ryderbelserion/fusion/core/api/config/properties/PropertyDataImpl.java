package com.ryderbelserion.fusion.core.api.config.properties;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.config.properties.objects.primitives.string.ListProperty;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.files.FileManager;
import io.leangen.geantyref.TypeToken;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import com.ryderbelserion.fusion.core.api.config.properties.builders.AliasBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyData;
import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@NullMarked
public final class PropertyDataImpl implements IPropertyData {

    private final FusionCore fusion = (FusionCore) FusionProvider.api();

    private final FileManager fileManager = this.fusion.getFileManager();

    private final PropertyDataBuilder propertyDataBuilder;
    private final Map<Object[], PropertyHolder> properties;
    private final Map<Object[], List<String>> comments;
    private final AliasBuilder aliasBuilder;

    public PropertyDataImpl(final PropertyDataBuilder propertyDataBuilder, final AliasBuilder aliasBuilder, final Map<Object[], PropertyHolder> properties, final Map<Object[], List<String>> comments) {
        this.propertyDataBuilder = propertyDataBuilder;
        this.aliasBuilder = aliasBuilder;
        this.properties = properties;
        this.comments = comments;
    }

    @Override
    public <T> T getProperty(final CommentedConfigurationNode configuration, final Property<T> property) {
        final Object[] path = property.getPath();

        if (!this.properties.containsKey(path)) {
            throw new IllegalStateException("The property %s is not present with this builder for %s! Perhaps wrong config!".formatted(path, this.propertyDataBuilder.getPath()));
        }

        final PropertyHolder propertyHolder = this.properties.get(path); // get property holder

        final PropertyType propertyType = property.getPropertyType();
        final T propertyValue = property.getDefaultValue();

        final Object[] propertyPath = getAlias(propertyHolder.getAlias(), path); // fetch alias from property holder with fallback path

        return switch (propertyType) {
            case STRING_LIST -> (T) StringUtils.getStringList(configuration.node(propertyPath), List.of(propertyValue.toString()));

            default -> {
                try {
                    this.fusion.log(true, Level.error, "<red>Property Type: %s", property.getType());

                    yield (T) configuration.node(propertyPath).get(property.getType(), propertyValue);
                } catch (final SerializationException exception) {
                    this.fusion.log(true, Level.error, "Failed to fetch property value for %s from %s! We are supplying the default value for now.", exception, Arrays.toString(propertyPath), this.propertyDataBuilder.getPath());

                    yield propertyValue;
                }
            }
        };
    }

    @Override
    public <T> void setProperty(final CommentedConfigurationNode configuration, final Property<T> property, final T value) {
        final Class<?> type = property.getType();

        if (type != value.getClass()) {
            return;
        }

        final Object[] path = property.getPath(); // get property path
        final PropertyHolder propertyHolder = this.properties.get(path); // get property holder

        final Object[] propertyPath = getAlias(propertyHolder.getAlias(), path); // fetch alias from property holder with fallback path

        switch (property.getPropertyType()) {
            case STRING_LIST -> {
                try {
                    configuration.node(propertyPath).setList(TypeToken.get(String.class), List.of(value.toString()));
                } catch (final SerializationException exception) {
                    this.fusion.log(true, Level.error, "Failed to set property value for %s in %s!", exception, Arrays.toString(propertyPath), this.propertyDataBuilder.getPath());
                }
            }

            default -> {
                try {
                    configuration.node(propertyPath).set(type, value);
                } catch (final SerializationException exception) {
                    this.fusion.log(true, Level.error, "Failed to set property value for %s in %s!", exception, propertyPath, this.propertyDataBuilder.getPath());
                }
            }
        }

        this.fileManager.saveFile(this.propertyDataBuilder.getPath());
    }

    @Override
    public <T> boolean hasProperty(final CommentedConfigurationNode configuration, final Property<T> property) {
        final Object[] path = property.getPath(); // get path
        final PropertyHolder propertyHolder = this.properties.get(path); // get property holder

        return configuration.hasChild(getAlias(propertyHolder.getAlias(), path)); // fetch alias from property holder with fallback path
    }

    @Override
    public void setComment(final CommentedConfigurationNode configuration, final String value, final Object... path) {
        configuration.node(path).commentIfAbsent(value);

        this.fileManager.saveFile(this.propertyDataBuilder.getPath());
    }

    @Override
    public void populate(final CommentedConfigurationNode configuration) {
        for (final Map.Entry<Object[], PropertyHolder> parent : this.properties.entrySet()) {
            final PropertyHolder holder = parent.getValue();
            final Property<?> property = holder.getProperty();

            if (holder.isDisabled()) continue;

            final Object[] id = getAlias(holder.getAlias(), property.getPath()); // fetch alias from property holder with fallback path

            final CommentedConfigurationNode node = configuration.node(id);

            if (holder.hasComments() && configuration.hasChild(id)) {
                final List<String> comments = holder.getComments();

                if (comments.isEmpty()) continue;

                node.commentIfAbsent(StringUtils.toString(comments));

                continue;
            }

            final Class<?> type = property.getType();

            if (!configuration.hasChild(id)) {
                switch (property.getPropertyType()) {
                    case STRING_LIST -> {
                        try {
                            if (property instanceof ListProperty listProperty) {
                                node.setList(TypeToken.get(String.class), listProperty.getDefaultValue());
                            }
                        } catch (final SerializationException exception) {
                            this.fusion.log(true, Level.error, "Failed to set property value for %s in %s!", exception, id, this.propertyDataBuilder.getPath());
                        }
                    }

                    default -> {
                        try {
                            node.set(type, property.getDefaultValue());
                        } catch (final SerializationException exception) {
                            this.fusion.log(true, Level.error, "Failed to set property value for %s in %s!", exception, id, this.propertyDataBuilder.getPath());
                        }
                    }
                }
            }
        }

        for (final Map.Entry<Object[], List<String>> parent : this.comments.entrySet()) {
            final List<String> list = parent.getValue();

            if (list.isEmpty()) continue;

            final Object[] id = parent.getKey();

            if (!configuration.hasChild(id)) continue;

            final CommentedConfigurationNode section = configuration.node(id);

            section.commentIfAbsent(StringUtils.toString(list));
        }

        this.fileManager.saveFile(this.propertyDataBuilder.getPath());
    }

    @Override
    public Object[] getAlias(final String alias, final Object... fallback) {
        return this.aliasBuilder.hasAlias(alias) ? Stream.of(this.aliasBuilder.getAlias(alias), fallback).flatMap(Stream::of).toArray(Object[]::new) : fallback;
    }
}