package com.ryderbelserion.fusion.core.api.config.properties.objects.primitives;

import com.ryderbelserion.fusion.core.api.config.properties.objects.BaseProperty;
import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LongProperty extends BaseProperty<Long> {

    public LongProperty(final long defaultValue, final String alias, final Object... path) {
        super(Long.class, defaultValue, alias, PropertyType.LONG, path);
    }

    public LongProperty(final long defaultValue, final Object... path) {
        this(defaultValue, "", path);
    }
}