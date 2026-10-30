package com.ryderbelserion.fusion.core.api.config.properties.objects;

import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EnumProperty<E extends Enum<E>> extends BaseProperty<E> {

    public EnumProperty(final Class<E> type, final E defaultValue, final Object[] path) {
        super(type, defaultValue, "", PropertyType.ENUM_PROPERTY, path);
    }
}