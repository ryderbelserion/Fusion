package com.ryderbelserion.fusion.config.properties.interfaces;

import com.ryderbelserion.fusion.config.properties.builders.AliasBuilder;
import com.ryderbelserion.fusion.config.properties.builders.CommentsBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IPropertyHolder {

    default void registerComments(final CommentsBuilder builder) {

    }

    default void registerAliases(final AliasBuilder builder) {

    }
}