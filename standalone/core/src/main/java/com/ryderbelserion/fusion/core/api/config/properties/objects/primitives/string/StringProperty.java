package com.ryderbelserion.fusion.core.api.config.properties.objects.primitives.string;

import org.jspecify.annotations.NullMarked;
import com.ryderbelserion.fusion.core.api.config.properties.objects.BaseProperty;
import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;

@NullMarked
public final class StringProperty extends BaseProperty {

    public StringProperty(final String defaultValue, final String alias, final Object... path) {
        super(String.class, defaultValue, alias, PropertyType.STRING, path);
    }

    public StringProperty(final String defaultValue, final Object... path) {
        this(defaultValue, "", path);
    }
}