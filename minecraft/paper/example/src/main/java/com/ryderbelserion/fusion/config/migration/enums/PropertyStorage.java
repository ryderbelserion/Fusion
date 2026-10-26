package com.ryderbelserion.fusion.config.migration.enums;

import com.ryderbelserion.fusion.config.keys.ConfigKeys;
import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyData;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.HashMap;
import java.util.Map;
import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newProperty;

public enum PropertyStorage {

    test_boolean(ConfigKeys.test_boolean, newProperty(false, "test", "section", "beidou", "toggle"));

    private final Property newProperty;
    private final Property oldProperty;

    PropertyStorage(final Property newProperty, final Property oldProperty) {
        this.newProperty = newProperty;
        this.oldProperty = oldProperty;
    }

    public boolean copy(final CommentedConfigurationNode configuration, final IPropertyData propertyData) {
        final Object[] path = this.oldProperty.getPath();

        if (!configuration.hasChild(path)) {
            return false;
        }

        try {
            final CommentedConfigurationNode node = configuration.node(path);

            propertyData.setProperty(configuration, this.newProperty, node.get(this.oldProperty.getType(), this.newProperty.getDefaultValue()));

            final CommentedConfigurationNode parent = node.parent();

            if (parent != null) {
                final Map<Object, CommentedConfigurationNode> children = new HashMap<>(parent.childrenMap());

                final int position = path.length - 1;

                final Object value = path[position];

                if (children.containsKey(value)) {
                    final CommentedConfigurationNode child = children.remove(value);

                    child.set(null);
                }

                if (children.isEmpty()) {
                    parent.set(null);
                }
            }
        } catch (final SerializationException exception) {
            throw new RuntimeException(exception);
        }

        return true;
    }
}