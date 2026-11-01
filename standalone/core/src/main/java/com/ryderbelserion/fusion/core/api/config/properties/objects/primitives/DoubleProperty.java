package com.ryderbelserion.fusion.core.api.config.properties.objects.primitives;

import com.ryderbelserion.fusion.core.api.config.properties.objects.BaseProperty;
import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DoubleProperty extends BaseProperty<Double> {

    public DoubleProperty(final double defaultValue, final String alias, final Object... path) {
        super(Double.class, defaultValue, alias, PropertyType.DOUBLE, path);
    }

    public DoubleProperty(final double defaultValue, final Object... path) {
        this(defaultValue, "", path);
    }
}