package com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces;

import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Property<T> {

    PropertyType getPropertyType();

    T getDefaultValue();

    Object[] getPath();

    String getAlias();

    Class<?> getType();

}