package com.ryderbelserion.fusion.core.api.config.properties.interfaces;

import com.ryderbelserion.fusion.core.api.config.properties.builders.AliasBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.builders.CommentsBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IPropertyHolder {

    default void registerComments(final CommentsBuilder builder) {

    }

    default void registerAliases(final AliasBuilder builder) {

    }
}