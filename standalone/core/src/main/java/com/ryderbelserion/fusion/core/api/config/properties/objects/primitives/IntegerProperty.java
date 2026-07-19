package com.ryderbelserion.fusion.core.api.config.properties.objects.primitives;

import org.jspecify.annotations.NullMarked;
import com.ryderbelserion.fusion.core.api.config.properties.objects.BaseProperty;
import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;

@NullMarked
public final class IntegerProperty extends BaseProperty {

    public IntegerProperty(final int defaultValue, final String alias, final Object... path) {
        super(Integer.class, defaultValue, alias, PropertyType.INTEGER, path);
    }

    public IntegerProperty(final int defaultValue, final Object... path) {
        this(defaultValue, "", path);
    }
}